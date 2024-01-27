package com.example.gitapp.ui.diagram.models

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

}
