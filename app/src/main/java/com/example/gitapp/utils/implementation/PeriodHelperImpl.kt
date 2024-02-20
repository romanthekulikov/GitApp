package com.example.gitapp.utils.implementation

import com.example.gitapp.data.api.models.StaredModel
import com.example.gitapp.entity.Stared
import com.example.gitapp.entity.Stargazer
import com.example.gitapp.entity.User
import com.example.gitapp.ui.diagram.PeriodType
import com.example.gitapp.utils.PeriodHelper
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

class PeriodHelperImpl @Inject constructor() : PeriodHelper {

    override fun getPeriodString(partData: List<Stared>, periodType: PeriodType): String {
        var startPeriodPart = partData[0].time
        return when (periodType) {
            PeriodType.WEEK -> {
                startPeriodPart.toString() //Just one day
            }

            PeriodType.MONTH -> {
                val endPeriodPartLocalDate = startPeriodPart.with(DayOfWeek.SUNDAY)
                startPeriodPart = endPeriodPartLocalDate.with(DayOfWeek.MONDAY)
                "$startPeriodPart <-> $endPeriodPartLocalDate"
            }

            PeriodType.YEAR -> {
                val endPeriodPart = startPeriodPart.withDayOfMonth(startPeriodPart.lengthOfMonth())
                startPeriodPart = endPeriodPart.withDayOfMonth(1)
                "$startPeriodPart <-> $endPeriodPart"
            }
        }
    }

    override fun getStarred(stargazersItemsList: List<Stargazer>): List<Stared> {
        val starredList = mutableListOf<StaredModel>()
        val tempStargazersList = stargazersItemsList.toMutableList()
        var listUser: List<User>

        while (tempStargazersList.size > 0) {
            val stargazer = tempStargazersList[0]
            val equalTimeStargazersList = tempStargazersList.filter { it.time == stargazer.time }
            listUser = equalTimeStargazersList.map { it.user }
            starredList.add(StaredModel(stargazer.time, listUser))
            tempStargazersList.removeAll(equalTimeStargazersList)
        }

        return starredList
    }

    override fun getDataInPeriod(startPeriod: LocalDate, endPeriod: LocalDate, staredList: List<Stared>): List<Stared> {
        return staredList.filter { it.time in startPeriod..endPeriod }
    }
}