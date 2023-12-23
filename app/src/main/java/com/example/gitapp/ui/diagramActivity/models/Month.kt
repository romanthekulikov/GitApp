package com.example.gitapp.ui.diagramActivity.models

data class Month(
    val first: MutableList<Weak> = mutableListOf(),
    val second: MutableList<Weak> = mutableListOf(),
    val third: MutableList<Weak> = mutableListOf(),
    val fourth: MutableList<Weak> = mutableListOf()
)
