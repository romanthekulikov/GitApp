package com.example.gitapp.ui.diagram.utils

import com.example.gitapp.ui.diagram.PeriodType
import com.example.gitapp.entity.Stared
import com.example.gitapp.entity.Stargazer
import java.time.LocalDate

interface PeriodHelper {
    fun getPeriodString(partData: List<Stared>, periodType: PeriodType): String
    fun getStarred(stargazersItemsList: List<Stargazer>): List<Stared>
    fun getDataInPeriod(startPeriod: LocalDate, endPeriod: LocalDate, staredList: List<Stared>): List<Stared>
    fun getMonthWeekPeriodArray(startPeriod: LocalDate, endPeriod: LocalDate): Array<String>
}