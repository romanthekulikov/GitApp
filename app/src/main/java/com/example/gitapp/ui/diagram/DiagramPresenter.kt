package com.example.gitapp.ui.diagram

import android.util.Log
import android.view.View
import com.example.gitapp.data.PeriodType
import com.example.gitapp.data.api.GitApiService
import com.example.gitapp.data.api.ITEM_PER_STARGAZERS_PAGE
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.injection.AppComponent
import com.example.gitapp.injection.factories.IndexAxisValueFormatterFactory
import com.example.gitapp.ui.base.BasePresenter
import com.example.gitapp.ui.base.ERROR_EXCEEDED_LIMIT
import com.example.gitapp.ui.base.ERROR_GITHUB_IS_SHUTDOWN
import com.example.gitapp.ui.base.ERROR_NO_INTERNET
import com.example.gitapp.ui.base.ERROR_NO_DATA
import com.example.gitapp.ui.diagram.models.Month
import com.example.gitapp.ui.diagram.models.Week
import com.example.gitapp.ui.diagram.models.Year
import com.example.gitapp.utils.HistogramPeriodAdapter
import com.example.gitapp.utils.PeriodHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@InjectViewState
class DiagramPresenter(
    private val repositoryName: String,
    private val ownerName: String,
    private val ownerIconUrl: String,
    private val stargazersCount: Int,
    appComponent: AppComponent
) : BasePresenter<DiagramView>() {
    init {
        appComponent.inject(this)
        resetPresenter()
        displayHistogramWithLoadData()
        displayRepo()
        viewState.setPreviousButtonEnabled(false)
    }

    private val weekDay = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val yearMonth = arrayOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")

    @Inject
    lateinit var periodHelper: PeriodHelper

    @Inject
    lateinit var histogramPeriodAdapter: HistogramPeriodAdapter

    @Inject
    lateinit var currentStargazersWeek: Week

    @Inject
    lateinit var currentStargazersMonth: Month

    @Inject
    lateinit var currentStargazersYear: Year

    @Inject
    lateinit var apiService: GitApiService

    @Inject
    lateinit var indexAxisValueFormatterFactory: IndexAxisValueFormatterFactory.Factory

    private var displayedDiagramPage = 0
    private var nextLoadPageNumber = (stargazersCount / ITEM_PER_STARGAZERS_PAGE) + 1
    private var stargazersItemsList: MutableList<ApiStarredData> = mutableListOf()
    private var enoughData = false
    private var toStartPeriodReplaced = false
    private var diagramMode = PeriodType.WEEK
    private var firstLoadedStargazerDate = LocalDate.now().with(DayOfWeek.MONDAY)
    private var lastDateLoadedStargazer = LocalDate.now().with(DayOfWeek.SUNDAY)
    private var startPeriod = LocalDate.now().with(DayOfWeek.MONDAY)!!
    private var endPeriod = LocalDate.now().with(DayOfWeek.SUNDAY)!!

    private fun resetPresenter() {
        nextLoadPageNumber = (stargazersCount / ITEM_PER_STARGAZERS_PAGE) + 1
        enoughData = false
        toStartPeriodReplaced = false
        startPeriod = LocalDate.now().with(DayOfWeek.MONDAY)
        endPeriod = LocalDate.now().with(DayOfWeek.SUNDAY)
        diagramMode = PeriodType.WEEK
        stargazersItemsList = mutableListOf()
        firstLoadedStargazerDate = LocalDate.now().with(DayOfWeek.MONDAY)
        displayedDiagramPage = 0
    }

    private fun displayHistogramWithLoadData() {
        viewState.changeVisibilityProgressBar(View.VISIBLE)
        launch {
            withContext(Dispatchers.IO) {
                detectDataShortage()
                while (!enoughData && nextLoadPageNumber > 0) {
                    try {
                        loadData()
                    } catch (e: UnknownHostException) {
                        displayErrorWithDataIfExist(message = ERROR_NO_INTERNET, logMessage = e.message)
                    } catch (e: RuntimeException) {
                        if (stargazersItemsList.isNotEmpty()) {
                            displayErrorWithDataIfExist(message = ERROR_EXCEEDED_LIMIT, logMessage = e.message)
                        } else {
                            displayErrorWithDataIfExist(message = ERROR_NO_DATA, logMessage = e.message)
                        }
                    } catch (e: SocketTimeoutException) {
                        displayErrorWithDataIfExist(message = ERROR_GITHUB_IS_SHUTDOWN, logMessage = e.message)
                    }
                }
            }
            withContext(Dispatchers.Main) {
                displayHistogram()
            }
        }
    }

    private fun displayRepo() {
        viewState.displayRepositoryItem(name = repositoryName, ownerIconUrl = ownerIconUrl)
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
        val loadedStargazers =
            apiService.fetchRepoStarred(ownerName = ownerName, repository = repositoryName, page = nextLoadPageNumber)
        stargazersItemsList = (loadedStargazers + stargazersItemsList).toMutableList()
        lastDateLoadedStargazer = stargazersItemsList[stargazersItemsList.size - 1].getLocalDate()
        firstLoadedStargazerDate = stargazersItemsList[0].getLocalDate()

        if (startPeriod > lastDateLoadedStargazer || !toStartPeriodReplaced) { // If repo don't have a star on the current period
            startPeriod = lastDateLoadedStargazer.with(DayOfWeek.MONDAY) // Use week cause presenter start with week period type
            endPeriod = lastDateLoadedStargazer.with(DayOfWeek.SUNDAY)
            toStartPeriodReplaced = true
        }

        if (firstLoadedStargazerDate < startPeriod || (loadedStargazers.size < 100 && nextLoadPageNumber == 1)) { //Gradual data loading
            enoughData = true
        }
        nextLoadPageNumber--
    }

    private suspend fun displayErrorWithDataIfExist(message: String, logMessage: String?) {
        enoughData = true
        withContext(Dispatchers.Main) {
            viewState.showError(message)
            viewState.setNextButtonEnabled(false)
            Log.e("api_retrofit", logMessage ?: message)
            if (stargazersItemsList.isNotEmpty()) {
                displayHistogram()
            }
        }
    }

    private fun displayHistogram() {
        if (nextLoadPageNumber == 0 && startPeriod < firstLoadedStargazerDate) {
            viewState.setNextButtonEnabled(false)
        }
        when (diagramMode) {
            PeriodType.WEEK -> displayWeakDiagram()
            PeriodType.MONTH -> displayMonthDiagram()
            PeriodType.YEAR -> displayYearDiagram()
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

    fun requestPartPeriodData(part: Int): Pair<List<ApiStarredData>, String> {
        return when (diagramMode) {
            PeriodType.WEEK -> periodHelper.getPartStargazersData(part, currentStargazersWeek, diagramMode)
            PeriodType.MONTH -> periodHelper.getPartStargazersData(part, currentStargazersMonth, diagramMode)
            PeriodType.YEAR -> periodHelper.getPartStargazersData(part, currentStargazersYear, diagramMode)
        }
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

    private fun displayWeakDiagram() {
        val week = periodHelper.getWeekStargazerByPeriod(startPeriod, endPeriod, stargazersItemsList)
        currentStargazersWeek = week
        val periodText = "[$startPeriod]  <->  [$endPeriod]"
        val barData = histogramPeriodAdapter.periodToBarData(week, diagramMode, periodText)
        val valueFormatter = indexAxisValueFormatterFactory.create(weekDay).createIndexAxisValueFormatter()

        viewState.displayData(barData, valueFormatter)
    }

    private fun displayMonthDiagram() {
        val month = periodHelper.getMonthStargazerByPeriod(startPeriod, endPeriod, stargazersItemsList)
        currentStargazersMonth = month
        val periodText = "[$startPeriod]  <->  [$endPeriod]"
        val barData = histogramPeriodAdapter.periodToBarData(month, diagramMode, periodText)
        val valueFormatter = indexAxisValueFormatterFactory.create(arrayOf()).createIndexAxisValueFormatter()

        viewState.displayData(barData, valueFormatter)
    }

    private fun displayYearDiagram() {
        val year = periodHelper.getYearStargazerByStartYear(startPeriod, stargazersItemsList)
        currentStargazersYear = year
        val periodText = "[$startPeriod]  <->  [$endPeriod]"
        val barData = histogramPeriodAdapter.periodToBarData(year, diagramMode, periodText)
        val valueFormatter = indexAxisValueFormatterFactory.create(yearMonth).createIndexAxisValueFormatter()

        viewState.displayData(barData, valueFormatter)
    }
}