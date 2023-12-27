package com.example.gitapp.data.entity

interface RepoEntity {
    val id: Int
    val repositoryName: String
    val isPrivate: Boolean
    val owner: Owner
    val stargazersCount: Int
}

interface Owner {
    val name: String
    val avatarUrl: String
}