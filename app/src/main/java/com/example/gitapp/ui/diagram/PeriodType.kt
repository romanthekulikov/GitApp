package com.example.gitapp.ui.diagram

enum class PeriodType {
    WEEK {
        override fun toString(): String {
            return "week"
        }
    },
    MONTH {
        override fun toString(): String {
            return "month"
        }
    },
    YEAR {
        override fun toString(): String {
            return "year"
        }
    }
}