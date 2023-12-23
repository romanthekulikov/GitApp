package com.example.gitapp.retrofit

import com.example.gitapp.retrofit.entities.GitRepositoryEntity
import com.example.gitapp.retrofit.entities.GitStarredEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitApiService {
    @GET("/users/{user}/repos")
    fun fetchOwnerRepositories(
        @Path(value = "user") ownerName: String,
        @Query(value = "per_page") itemInPageCount: Int
    ): Call<List<GitRepositoryEntity>> //TODO:

    @Headers("Accept: application/vnd.github.v3.star+json")
    @GET("repos/{user}/{repos}/stargazers")
    fun fetchRepositoriesStarred(
        @Path(value = "user") ownerName: String,
        @Path(value = "repos") repository: String,
        @Query(value = "per_page") itemInPageCount: Int,
        @Query(value = "page") page: Int
    ): Call<List<GitStarredEntity>>
}