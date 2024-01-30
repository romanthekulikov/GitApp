package com.example.gitapp.ui.diagram.models

import com.example.gitapp.data.api.models.ApiStarredData
import javax.inject.Inject

data class Week(
    val weekDays: MutableList<MutableList<ApiStarredData>> = mutableListOf(mutableListOf())
) : Period {

    init {
        for (i in 0..6) {
            weekDays.add(mutableListOf())
        }
    }
    override fun getStargazerCount(): Int {
        var count = 0
        for (day in weekDays) {
            count += day.size
        }
        return count
    }
}
