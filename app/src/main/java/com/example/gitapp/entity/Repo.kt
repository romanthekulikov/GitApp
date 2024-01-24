package com.example.gitapp.entity

interface Repo {
    val id: Int
    val name: String
    val isPrivate: Boolean
    val owner: Owner
    val stargazersCount: Int
}