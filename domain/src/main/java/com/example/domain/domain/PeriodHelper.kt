package com.example.domain.domain

import com.example.domain.domain.entity.Stared
import com.example.domain.domain.entity.Stargazer
import java.time.LocalDate

interface PeriodHelper {
    /**
     * Return date diapason for starred data. Return string such as "2024-03-29" or
     * "2024-03-25 <-> 2024-03-31" relatively PeriodType
     */
    fun getPeriodString(partData: List<Stared>, periodType: String): String

    /**
     * Sorts stargazers into Stared by stargazer time
     */
    fun getStarred(stargazersItemsList: List<Stargazer>): List<Stared>

    /**
     * Return part of data in specified period
     */
    fun getDataInPeriod(startPeriod: LocalDate, endPeriod: LocalDate, staredList: List<Stared>): List<Stared>

    /**
     * Return array week period in specified period.
     * String such as 25-31, when 25 is Monday and 31 is Sunday
     */
    fun getMonthWeekPeriodArray(startPeriod: LocalDate, endPeriod: LocalDate): Array<String>
}