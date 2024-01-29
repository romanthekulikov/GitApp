package com.example.gitapp.utils

import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.ui.diagram.models.Month
import com.example.gitapp.ui.diagram.models.Week
import com.example.gitapp.ui.diagram.models.Year
import java.time.LocalDate

interface PeriodHelper {
    fun getWeekStargazerByPeriod(
        startWeek: LocalDate,
        endWeek: LocalDate,
        listPageItemsStargazers: MutableList<ApiStarredData>
    ): Week

    fun getMonthStargazerByPeriod(
        startMonth: LocalDate,
        endMonth: LocalDate,
        listPageItemsStargazers: MutableList<ApiStarredData>
    ): Month

    fun getYearStargazerByStartYear(startYear: LocalDate, listPageItemsStargazers: MutableList<ApiStarredData>): Year

}