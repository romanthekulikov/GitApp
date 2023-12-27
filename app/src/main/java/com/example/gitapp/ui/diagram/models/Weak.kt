package com.example.gitapp.ui.diagram.models

import com.example.gitapp.data.api.entities.GitStarredEntity

data class Weak(
    val monday: MutableList<GitStarredEntity> = mutableListOf(),
    val tuesday: MutableList<GitStarredEntity> = mutableListOf(),
    val wednesday: MutableList<GitStarredEntity> = mutableListOf(),
    val thursday: MutableList<GitStarredEntity> = mutableListOf(),
    val friday: MutableList<GitStarredEntity> = mutableListOf(),
    val saturday: MutableList<GitStarredEntity> = mutableListOf(),
    val sunday: MutableList<GitStarredEntity> = mutableListOf()
)
