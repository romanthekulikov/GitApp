package com.example.gitapp.ui.diagramActivity.models

import com.example.gitapp.retrofit.entities.GitStarredEntity

data class Month(
    val first: MutableList<GitStarredEntity> = mutableListOf(),
    val second: MutableList<GitStarredEntity> = mutableListOf(),
    val third: MutableList<GitStarredEntity> = mutableListOf(),
    val fourth: MutableList<GitStarredEntity> = mutableListOf()
)
