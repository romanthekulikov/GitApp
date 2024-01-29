package com.example.gitapp.ui.diagram.models

import java.io.Serializable

interface Period: Serializable {
    fun getStargazerCount(): Int
}