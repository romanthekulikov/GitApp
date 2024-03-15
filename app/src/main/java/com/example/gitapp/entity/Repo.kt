package com.example.gitapp.entity

import android.os.Parcelable

interface Repo : Parcelable {
    val name: String
    val owner: User
    var stargazersCount: Int
}