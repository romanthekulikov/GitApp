package com.example.gitapp.entity

interface Repo {
    val name: String
    val owner: User
    val stargazersCount: Int
}