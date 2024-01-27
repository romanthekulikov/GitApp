package com.example.gitapp.ui.diagram.models

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

}
