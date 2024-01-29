package com.example.gitapp.ui.diagram

import android.util.Log
import android.view.View
import com.example.gitapp.data.PeriodType
import com.example.gitapp.data.api.GitApiClient
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.ui.base.BasePresenter
import com.example.gitapp.utils.HistogramPeriodAdapter
import com.example.gitapp.utils.PeriodHelper
import com.example.gitapp.utils.implementation.HistogramPeriodAdapterImpl
import com.example.gitapp.utils.implementation.PeriodHelperImpl
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import java.lang.RuntimeException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@InjectViewState
class DiagramPresenter @Inject constructor(
    private val repositoryName: String,
    private val ownerName: String,
    private val ownerIconUrl: String,
    private val stargazersCount: Int
) : BasePresenter<DiagramView>() {
    init {
        resetPresenter()
        displayHistogramWithLoadData()
        displayRepository()
        viewState.changeNextButtonVisibility(View.GONE)
    }

    private val weakDay = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    private val yearMonth = arrayOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")
    private var listPageItemsStargazers: MutableList<ApiStarredData> = mutableListOf()
    private var downloadPage = (stargazersCount / 100) + 1
    private var enoughData = false
    private var firstDateLoadedStargazer = LocalDate.now().with(DayOfWeek.MONDAY)
    private var startPeriod = LocalDate.now().with(DayOfWeek.MONDAY)
    private var endPeriod = LocalDate.now().with(DayOfWeek.SUNDAY)
    private var diagramMode = PeriodType.WEAK

    private fun resetPresenter() {
        downloadPage = (stargazersCount / 100) + 1
        enoughData = false
        startPeriod = LocalDate.now().with(DayOfWeek.MONDAY)
        endPeriod = LocalDate.now().with(DayOfWeek.SUNDAY)
        diagramMode = PeriodType.WEAK
        listPageItemsStargazers = mutableListOf()
        firstDateLoadedStargazer = LocalDate.now().with(DayOfWeek.MONDAY)
    }

    private fun displayHistogramWithLoadData() {
        viewState.changeVisibilityProgressBar(View.VISIBLE)
        launch {
            withContext(Dispatchers.IO) {
                detectDataShortage()
                while (!enoughData && downloadPage > 0) {
                    try {
                        loadData()
                    } catch (e: Exception) {
                        enoughData = true

                    } catch (e: UnknownHostException) {
                        displayErrorWithDataIfExist(e.message ?: "No internet")
                    } catch (e: RuntimeException) {
                        displayErrorWithDataIfExist(e.message ?: "Timed out")
                    } catch (e: SocketTimeoutException) {
                        displayErrorWithDataIfExist(e.message ?: "Github is shutdown")
                    }
                }
            }
            withContext(Dispatchers.Main) {
                displayHistogram()
            }
        }
    }

    private fun displayRepository() {
        viewState.displayRepositoryItem(name = repositoryName, ownerIconUrl = ownerIconUrl)
    }

    private fun detectDataShortage() {
        if (firstDateLoadedStargazer == null) {
            return
        }
        if (firstDateLoadedStargazer >= startPeriod) {
            enoughData = false
        }
    }

    private suspend fun loadData() {
        val starClient = GitApiClient
            .apiService
            .fetchRepositoriesStarred(ownerName, repositoryName, 100, downloadPage)
        listPageItemsStargazers = (starClient + listPageItemsStargazers).toMutableList()
        val lastDateLoadedStargazer = listPageItemsStargazers[listPageItemsStargazers.size - 1].getLocalDate()
        firstDateLoadedStargazer = listPageItemsStargazers[0].getLocalDate()

        if (startPeriod > lastDateLoadedStargazer) {//If repo don't have a star on the current weak
            startPeriod = lastDateLoadedStargazer.with(DayOfWeek.MONDAY)
            endPeriod = lastDateLoadedStargazer.with(DayOfWeek.SUNDAY)
        }

        if (firstDateLoadedStargazer < startPeriod || (starClient.size < 100 && downloadPage == 1)) { //Gradual data loading
            enoughData = true
        }
        downloadPage--
    }

    private suspend fun displayErrorWithDataIfExist(message: String) {
        withContext(Dispatchers.Main) {
            viewState.showError("Fetch stargazers error")
            Log.e("api_retrofit", message)
            if (listPageItemsStargazers.isNotEmpty()) {
                displayHistogram()
            }
        }
    }

    private fun displayHistogram() {
        if (downloadPage == 0 && startPeriod < firstDateLoadedStargazer) {
            viewState.changePreviousButtonVisibility(View.GONE)
        }
        when (diagramMode) {
            PeriodType.WEAK -> {
                displayWeakDiagram()
            }

            PeriodType.MONTH -> {
                displayMonthDiagram()
            }

            PeriodType.YEAR -> {
                displayYearDiagram()
            }
        }

        viewState.changeVisibilityProgressBar(View.GONE)
    }

    fun requestMoveToNextHistogramPage() {
        viewState.changePreviousButtonVisibility(View.VISIBLE)
        if (startPeriod == LocalDate.now().minusWeeks(1).with(DayOfWeek.MONDAY)) {
            viewState.changeNextButtonVisibility(View.GONE)
        }
        moveBackPeriod()
        displayHistogramWithLoadData()
    }

    fun requestMoveToPreviousHistogramPage() {
        viewState.changeNextButtonVisibility(View.VISIBLE)
        moveForwardPeriod()
        displayHistogramWithLoadData()
    }

    fun requestChangeDiagramMode(mode: PeriodType) {
        diagramMode = mode
        val now = LocalDate.now()
        when (diagramMode) {
            PeriodType.WEAK -> {
                startPeriod = now.with(DayOfWeek.MONDAY)
                endPeriod = now.with(DayOfWeek.SUNDAY)
            }

            PeriodType.MONTH -> {
                startPeriod = now.withDayOfMonth(1)
                endPeriod = now.withDayOfMonth(now.lengthOfMonth())
            }

            PeriodType.YEAR -> {
                startPeriod = now.withDayOfYear(1)
                endPeriod = now.withDayOfYear(now.lengthOfYear())
            }
        }
        viewState.changeNextButtonVisibility(View.GONE)
        displayHistogramWithLoadData()
    }

    private fun moveBackPeriod() {
        when (diagramMode) {
            PeriodType.WEAK -> {
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

    private fun moveForwardPeriod() {
        when (diagramMode) {
            PeriodType.WEAK -> {
                startPeriod = startPeriod.minusWeeks(1)
                endPeriod = endPeriod.minusWeeks(1)
            }

            PeriodType.MONTH -> {
                startPeriod = startPeriod.minusMonths(1)
                endPeriod = endPeriod.minusMonths(1)
            }

            PeriodType.YEAR -> {
                startPeriod = startPeriod.minusYears(1)
                endPeriod = endPeriod.minusYears(1)
            }
        }
    }

    private fun displayWeakDiagram() {
        val periodHelper: PeriodHelper = PeriodHelperImpl()
        val histogramPeriodAdapter: HistogramPeriodAdapter = HistogramPeriodAdapterImpl()
        val weak = periodHelper.getWeekStargazerByPeriod(startPeriod, endPeriod, listPageItemsStargazers)
        val periodText = "[$startPeriod]  <->  [$endPeriod]"
        val barData = histogramPeriodAdapter.periodToBarData(weak, diagramMode, periodText)
        val valueFormatter = IndexAxisValueFormatter(weakDay)

        viewState.displayData(barData, valueFormatter)
    }

    private fun displayMonthDiagram() {
        val periodHelper: PeriodHelper = PeriodHelperImpl()
        val histogramPeriodAdapter: HistogramPeriodAdapter = HistogramPeriodAdapterImpl()
        val month = periodHelper.getMonthStargazerByPeriod(startPeriod, endPeriod, listPageItemsStargazers)
        val periodText = "[$startPeriod]  <->  [$endPeriod]"
        val barData = histogramPeriodAdapter.periodToBarData(month, diagramMode, periodText)
        val valueFormatter = IndexAxisValueFormatter()

        viewState.displayData(barData, valueFormatter)
    }

    private fun displayYearDiagram() {
        val periodHelper: PeriodHelper = PeriodHelperImpl()
        val histogramPeriodAdapter: HistogramPeriodAdapter = HistogramPeriodAdapterImpl()
        val year = periodHelper.getYearStargazerByStartYear(startPeriod, listPageItemsStargazers)
        val periodText = "[$startPeriod]  <->  [$endPeriod]"
        val barData = histogramPeriodAdapter.periodToBarData(year, diagramMode, periodText)
        val valueFormatter = IndexAxisValueFormatter(yearMonth)

        viewState.displayData(barData, valueFormatter)
    }
}