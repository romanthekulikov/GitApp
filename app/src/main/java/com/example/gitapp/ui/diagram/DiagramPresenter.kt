package com.example.gitapp.ui.diagram

import android.util.Log
import android.view.View
import com.example.domain.domain.use_cases.diagram.AdaptPeriodUseCase
import com.example.domain.domain.use_cases.diagram.ConvertPeriodUseCase
import com.example.domain.domain.Repository
import com.example.domain.domain.entity.Stared
import com.example.domain.domain.models.RepoEntity
import com.example.gitapp.GitApp
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
    lateinit var convertPeriodUseCase: ConvertPeriodUseCase

    @Inject
    lateinit var adaptPeriodUseCase: AdaptPeriodUseCase //pattern

    @Inject
    lateinit var repository: Repository

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
        GitApp.appComponent.inject(this)
        resetPresenter()
        repository.clearMemorySavedStargazers()
        displayHistogramWithLoadData()
        displayRepo()
        viewState.setPreviousButtonEnabled(false)
        launch { repository.updateRepoStargazersCount(repo.owner.nameUser, repo.name, repo.stargazersCount) }
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
            while (!enoughData && nextLoadPageNumber > 0 && repo.stargazersCount > 0) {
                try {
                    loadData()
                } catch (e: UnknownHostException) {
                    displayErrorWithDataIfExist(message = ERROR_NO_INTERNET, logMessage = e.message)
                } catch (e: HttpException) {
                    if (repository.getLoadedStargazers().isNotEmpty()) {
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
            val loadedStargazers = repository.getStargazersList(repo.owner.nameUser, repo.name, nextLoadPageNumber)
            fillFields(loadedStargazers)
            nextLoadPageNumber--
        } catch (e: UnknownHostException) {
            val loadedStargazers = repository.getStargazersList(repo.owner.nameUser, repo.name)
            if (loadedStargazers.isEmpty()) {
                throw e
            }
            fillFields(loadedStargazers)

            throw e
        }
    }

    private fun fillFields(loadedStargazers: List<com.example.domain.domain.entity.Stargazer>) {
        lastDateLoadedStargazer = repository.getLastLoadedStargazerDate()
        firstLoadedStargazerDate = repository.getFirstLoadedStargazerDate()

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
        if (repository.getLoadedStargazers().isNotEmpty()) {
            displayHistogram()
        }
    }

    private fun displayHistogram() {
        if (nextLoadPageNumber == 0 && startPeriod < firstLoadedStargazerDate) {
            viewState.setNextButtonEnabled(false)
        }
        val loadedData = repository.getLoadedDataInPeriod(startPeriod, endPeriod)
        val barData = adaptPeriodUseCase.periodToBarData(loadedData, diagramMode.toString(), startPeriod, endPeriod)
        when (diagramMode) {
            PeriodType.WEEK -> viewState.displayData(
                barData,
                IndexAxisValueFormatter(weekDay)
            )

            PeriodType.MONTH -> viewState.displayData(
                barData,
                IndexAxisValueFormatter(convertPeriodUseCase.getMonthWeekPeriodArray(startPeriod, endPeriod))
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

    fun requestPeriodDataTime(periodData: List<Stared>): String {
        return convertPeriodUseCase.getPeriodString(periodData, diagramMode.toString())
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