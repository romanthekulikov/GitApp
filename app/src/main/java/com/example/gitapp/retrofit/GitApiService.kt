package com.example.gitapp.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitApiService {
    @GET("/users/{user}/repos")
    fun fetchOwnerRepositories(@Path(value = "user") ownerName: String): Call<List<GitRepositoryResponse>> //TODO:
}