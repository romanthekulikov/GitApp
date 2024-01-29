package com.example.gitapp.ui.diagram.models

import com.example.gitapp.data.api.models.ApiStarredData

data class Year(
    val months: MutableList<Month> = mutableListOf()
) : Period {
    override fun getStargazerCount(): Int {
        var count = 0
        for (week in months) {
            count += week.getStargazerCount()
        }

        return count
    }

    fun getStargazersFromMonth(monthNumber: Int): MutableList<ApiStarredData> {
        val stargazersList = mutableListOf<ApiStarredData>()
        val requireMonth = months[monthNumber]
        for (week in requireMonth.weeks.indices) {
            stargazersList.addAll(requireMonth.getStargazersFromWeek(week))
        }

        return stargazersList
    }
}
