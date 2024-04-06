package com.example.domain.domain.entity

import android.os.Parcelable

interface Repo : Parcelable {
    val name: String
    val owner: User
    var stargazersCount: Int
}