package com.example.gitapp.entity

import android.os.Parcelable

interface User : Parcelable {
    val nameUser: String
    val avatarUrl: String
}