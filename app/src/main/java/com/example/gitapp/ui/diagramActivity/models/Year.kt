package com.example.gitapp.ui.diagramActivity.models

import com.example.gitapp.retrofit.entities.GitStarredEntity

data class Year(
    val december: MutableList<GitStarredEntity> = mutableListOf(),
    val january: MutableList<GitStarredEntity> = mutableListOf(),
    val february: MutableList<GitStarredEntity> = mutableListOf(),
    val march: MutableList<GitStarredEntity> = mutableListOf(),
    val april: MutableList<GitStarredEntity> = mutableListOf(),
    val may: MutableList<GitStarredEntity> = mutableListOf(),
    val june: MutableList<GitStarredEntity> = mutableListOf(),
    val july: MutableList<GitStarredEntity> = mutableListOf(),
    val august: MutableList<GitStarredEntity> = mutableListOf(),
    val september: MutableList<GitStarredEntity> = mutableListOf(),
    val october: MutableList<GitStarredEntity> = mutableListOf(),
    val november: MutableList<GitStarredEntity> = mutableListOf(),
)
