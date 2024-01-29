package com.example.gitapp.ui.diagram.models

import com.example.gitapp.data.api.models.ApiStarredData

data class Month(
    val weeks: MutableList<Week> = mutableListOf()
) : Period {
    override fun getStargazerCount(): Int {
        var count = 0
        for (week in weeks) {
            count += week.getStargazerCount()
        }
        return count
    }

    fun getStargazersFromWeek(weekNumber: Int): MutableList<ApiStarredData> {
        val stargazersList = mutableListOf<ApiStarredData>()
        for (day in weeks[weekNumber].weekDays) {
            stargazersList.addAll(day)
        }
        return stargazersList
    }
}
