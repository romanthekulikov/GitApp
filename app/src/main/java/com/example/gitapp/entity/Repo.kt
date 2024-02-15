package com.example.gitapp.entity

interface Repo {
    val id: Int
    val name: String
    val owner: User
    val stargazersCount: Int
}