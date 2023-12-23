package com.example.gitapp.ui.diagramActivity.models

data class Year(
    val december: MutableList<Month> = mutableListOf(),
    val january: MutableList<Month> = mutableListOf(),
    val february: MutableList<Month> = mutableListOf(),
    val march: MutableList<Month> = mutableListOf(),
    val april: MutableList<Month> = mutableListOf(),
    val may: MutableList<Month> = mutableListOf(),
    val june: MutableList<Month> = mutableListOf(),
    val july: MutableList<Month> = mutableListOf(),
    val august: MutableList<Month> = mutableListOf(),
    val september: MutableList<Month> = mutableListOf(),
    val october: MutableList<Month> = mutableListOf(),
    val november: MutableList<Month> = mutableListOf()
)
