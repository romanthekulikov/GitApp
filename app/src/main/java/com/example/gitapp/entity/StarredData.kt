package com.example.gitapp.entity

import com.example.gitapp.data.api.models.ApiUser

interface StarredData {
    val timeString: String
    val user: ApiUser
}