package com.example.gitapp.utils

import com.example.gitapp.data.PeriodType
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.ui.diagram.models.Month
import com.example.gitapp.ui.diagram.models.Period
import com.example.gitapp.ui.diagram.models.Week
import com.example.gitapp.ui.diagram.models.Year
import java.time.LocalDate

interface PeriodHelper {
    fun getWeekStargazerByPeriod(
        startPeriod: LocalDate,
        endPeriod: LocalDate,
        listPageItemsStargazers: MutableList<ApiStarredData>
    ): Week

    fun getMonthStargazerByPeriod(
        startPeriod: LocalDate,
        endPeriod: LocalDate,
        listPageItemsStargazers: MutableList<ApiStarredData>
    ): Month

    fun getYearStargazerByStartYear(startYear: LocalDate, listPageItemsStargazers: MutableList<ApiStarredData>): Year

    fun getPartStargazersData(part: Int, period: Period, periodType: PeriodType): Pair<List<ApiStarredData>, String>

}