package com.example.gitapp.ui.diagram

import android.util.Log
import android.view.View
import com.example.domain.domain.HistogramPeriodAdapter
import com.example.domain.domain.PeriodHelper
import com.example.domain.domain.Repository
import com.example.domain.domain.models.RepoEntity
import com.example.domain.domain.use_cases.diagram.ClearMemorySavedStargazersUseCase
import com.example.domain.domain.use_cases.diagram.GetDateLoadedStargazerUseCase
import com.example.domain.domain.use_cases.diagram.GetLoadedDataInPeriodUseCase
import com.example.domain.domain.use_cases.diagram.GetStargazersListUseCase
import com.example.domain.domain.use_cases.diagram.UpdateRepoStargazersCountUseCase
import com.example.gitapp.App
import com.example.gitapp.ui.base.BasePresenter
import com.example.gitapp.ui.base.ERROR_EXCEEDED_LIMIT
import com.example.gitapp.ui.base.ERROR_GITHUB_IS_SHUTDOWN
import com.example.gitapp.ui.base.ERROR_NO_DATA
import com.example.gitapp.ui.base.ERROR_NO_INTERNET
import com.example.gitapp.ui.base.ERROR_UNIDENTIFIED
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch
import moxy.InjectViewState
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@InjectViewState
class DiagramPresenter(
    private val repo: RepoEntity
) : BasePresenter<DiagramView>() {
    private val weekDay = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val yearMonth = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    @Inject
    lateinit var periodHelper: PeriodHelper

    @Inject
    lateinit var histogramPeriodAdapter: HistogramPeriodAdapter //pattern

    @Inject
    lateinit var repository: Repository

    private val getStargazersListUseCase by lazy { GetStargazersListUseCase(repository) }
    private val getDateLoadedStargazerUseCase by lazy { GetDateLoadedStargazerUseCase(repository) }
    private val getLoadedDataInPeriodUseCase by lazy { GetLoadedDataInPeriodUseCase(repository) }
    private val clearMemorySavedStargazersUseCase by lazy { ClearMemorySavedStargazersUseCase(repository) }
    private val updateRepoStargazersCountUseCase by lazy { UpdateRepoStargazersCountUseCase(repository) }

    private var displayedDiagramPage = 0
    private var nextLoadPageNumber = (repo.stargazersCount / com.example.data.data.api.ITEM_PER_STARGAZERS_PAGE) + 1
    private var enoughData = false
    private var toStartPeriodMoved = false
    private var errorShowed = false
    private var diagramMode = PeriodType.WEEK
    private var firstLoadedStargazerDate = LocalDate.now().with(DayOfWeek.MONDAY)
    private var lastDateLoadedStargazer = LocalDate.now().with(DayOfWeek.SUNDAY)
    private var startPeriod = LocalDate.now().with(DayOfWeek.MONDAY)!!
    private var endPeriod = LocalDate.now().with(DayOfWeek.SUNDAY)!!

    init {
        App.appComponent.inject(this)
        resetPresenter()
        clearMemorySavedStargazersUseCase.execute()
        displayHistogramWithLoadData()
        displayRepo()
        viewState.setPreviousButtonEnabled(false)
        launch { updateRepoStargazersCountUseCase.execute(repo) }
    }

    private fun resetPresenter() {
        nextLoadPageNumber = (repo.stargazersCount / com.example.data.data.api.ITEM_PER_STARGAZERS_PAGE) + 1
        enoughData = false
        toStartPeriodMoved = false
        errorShowed = false
        startPeriod = LocalDate.now().with(DayOfWeek.MONDAY)
        endPeriod = LocalDate.now().with(DayOfWeek.SUNDAY)
        diagramMode = PeriodType.WEEK
        firstLoadedStargazerDate = LocalDate.now().with(DayOfWeek.MONDAY)
        displayedDiagramPage = 0
    }

    private fun displayHistogramWithLoadData() {
        viewState.changeVisibilityProgressBar(View.VISIBLE)
        launch {
            detectDataShortage()
            while (!enoughData && nextLoadPageNumber > 0) {
                try {
                    loadData()
                } catch (e: UnknownHostException) {
                    displayErrorWithDataIfExist(message = ERROR_NO_INTERNET, logMessage = e.message)
                } catch (e: HttpException) {
                    if (getStargazersListUseCase.execute().isNotEmpty()) {
                        displayErrorWithDataIfExist(message = ERROR_EXCEEDED_LIMIT, logMessage = e.message)
                    } else {
                        displayErrorWithDataIfExist(message = ERROR_NO_DATA, logMessage = e.message)
                    }
                } catch (e: SocketTimeoutException) {
                    displayErrorWithDataIfExist(message = ERROR_GITHUB_IS_SHUTDOWN, logMessage = e.message)
                } catch (e: IOException) {
                    displayErrorWithDataIfExist(message = ERROR_UNIDENTIFIED, logMessage = e.message)
                }
            }

            displayHistogram()
        }
    }

    private fun displayRepo() {
        viewState.displayRepositoryItem(name = repo.name, ownerIconUrl = repo.owner.avatarUrl, isFavorite = repo.isFavorite)
    }

    private fun detectDataShortage() {
        if (firstLoadedStargazerDate == null) {
            return
        }
        if (firstLoadedStargazerDate >= startPeriod) {
            enoughData = false
        }
    }

    private suspend fun loadData() {
        try {
            val loadedStargazers = getStargazersListUseCase.execute(repo, nextLoadPageNumber)
            fillFields(loadedStargazers)
            nextLoadPageNumber--
        } catch (e: UnknownHostException) {
            val loadedStargazers = getStargazersListUseCase.execute(repo)
            if (loadedStargazers.isEmpty()) {
                throw e
            }
            fillFields(loadedStargazers)

            throw e
        }
    }

    private fun fillFields(loadedStargazers: List<com.example.domain.domain.entity.Stargazer>) {
        lastDateLoadedStargazer = getDateLoadedStargazerUseCase.executeLast()
        firstLoadedStargazerDate = getDateLoadedStargazerUseCase.executeFirst()

        if (startPeriod > lastDateLoadedStargazer || !toStartPeriodMoved) { // If repo don't have a star on the current period
            startPeriod = lastDateLoadedStargazer.with(DayOfWeek.MONDAY) // Use week cause presenter start with week period type
            endPeriod = lastDateLoadedStargazer.with(DayOfWeek.SUNDAY)
            toStartPeriodMoved = true
        }

        if (firstLoadedStargazerDate < startPeriod || (loadedStargazers.size < 100 && nextLoadPageNumber == 1)) { //Gradual data loading
            enoughData = true
        }
    }

    private fun displayErrorWithDataIfExist(message: String, logMessage: String?) {
        enoughData = true
        if (!errorShowed) {
            viewState.showError(message)
        }
        errorShowed = true
        Log.e("api_retrofit", logMessage ?: message)
        if (getStargazersListUseCase.execute().isNotEmpty()) {
            displayHistogram()
        }
    }

    private fun displayHistogram() {
        if (nextLoadPageNumber == 0 && startPeriod < firstLoadedStargazerDate) {
            viewState.setNextButtonEnabled(false)
        }
        val loadedData = getLoadedDataInPeriodUseCase.execute(startPeriod, endPeriod)
        val barData = histogramPeriodAdapter.periodToBarData(loadedData, diagramMode.toString(), startPeriod, endPeriod)
        when (diagramMode) {
            PeriodType.WEEK -> viewState.displayData(
                barData,
                IndexAxisValueFormatter(weekDay)
            )

            PeriodType.MONTH -> viewState.displayData(
                barData,
                IndexAxisValueFormatter(periodHelper.getMonthWeekPeriodArray(startPeriod, endPeriod))
            )

            PeriodType.YEAR -> viewState.displayData(
                barData,
                IndexAxisValueFormatter(yearMonth)
            )
        }
        viewState.changeVisibilityProgressBar(View.GONE)
    }

    fun requestMoveToNextHistogramPage() {
        viewState.setPreviousButtonEnabled(true)
        moveBackPeriod()
        displayHistogramWithLoadData()
        displayedDiagramPage++
    }

    fun requestMoveToPreviousHistogramPage() {
        if (displayedDiagramPage == 1) {
            viewState.setPreviousButtonEnabled(false)
        }
        viewState.setNextButtonEnabled(true)
        moveForwardPeriod()
        displayHistogramWithLoadData()
        displayedDiagramPage--
    }

    fun requestChangeDiagramMode(mode: PeriodType) {
        diagramMode = mode
        when (diagramMode) {
            PeriodType.WEEK -> {
                startPeriod = lastDateLoadedStargazer.with(DayOfWeek.MONDAY)
                endPeriod = lastDateLoadedStargazer.with(DayOfWeek.SUNDAY)
            }

            PeriodType.MONTH -> {
                startPeriod = lastDateLoadedStargazer.withDayOfMonth(1)
                endPeriod = lastDateLoadedStargazer.withDayOfMonth(lastDateLoadedStargazer.lengthOfMonth())
            }

            PeriodType.YEAR -> {
                startPeriod = lastDateLoadedStargazer.withDayOfYear(1)
                endPeriod = lastDateLoadedStargazer.withDayOfYear(lastDateLoadedStargazer.lengthOfYear())
            }
        }
        displayedDiagramPage = 0
        viewState.setPreviousButtonEnabled(false)
        viewState.setNextButtonEnabled(true)
        displayHistogramWithLoadData()
    }

    fun requestPeriodDataTime(periodData: List<com.example.domain.domain.entity.Stared>): String {
        return periodHelper.getPeriodString(periodData, diagramMode.toString())
    }

    private fun moveBackPeriod() {
        when (diagramMode) {
            PeriodType.WEEK -> {
                startPeriod = startPeriod.minusWeeks(1)
                endPeriod = endPeriod.minusWeeks(1)
            }

            PeriodType.MONTH -> {
                startPeriod = startPeriod.minusMonths(1)
                endPeriod = endPeriod.minusMonths(1)
                endPeriod = endPeriod.withDayOfMonth(endPeriod.lengthOfMonth())
            }

            PeriodType.YEAR -> {
                startPeriod = startPeriod.minusYears(1)
                endPeriod = endPeriod.minusYears(1)
                endPeriod = endPeriod.withDayOfYear(endPeriod.lengthOfYear())
            }
        }
    }

    private fun moveForwardPeriod() {
        when (diagramMode) {
            PeriodType.WEEK -> {
                startPeriod = startPeriod.plusWeeks(1)
                endPeriod = endPeriod.plusWeeks(1)
            }

            PeriodType.MONTH -> {
                startPeriod = startPeriod.plusMonths(1)
                endPeriod = endPeriod.plusMonths(1)
            }

            PeriodType.YEAR -> {
                startPeriod = startPeriod.plusYears(1)
                endPeriod = endPeriod.plusYears(1)
            }
        }
    }
}