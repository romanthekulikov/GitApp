package com.example.gitapp.data.api

import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.data.api.models.ApiStarredData
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GitApiService {
    @GET("/users/{user}/repos")
    suspend fun fetchOwnerRepos(
        @Path(value = "user") ownerName: String,
        @Query(value = "page") numberOfPage: Int,
        @Query(value = "per_page") numberOfItemInPage: Int = 100
    ): List<ApiRepo>

    @Headers("Accept: application/vnd.github.v3.star+json")
    @GET("repos/{user}/{repos}/stargazers")
    suspend fun fetchRepositoriesStarred(
        @Path(value = "user") ownerName: String,
        @Path(value = "repos") repository: String,
        @Query(value = "per_page") itemInPageCount: Int,
        @Query(value = "page") page: Int
    ): List<ApiStarredData>
}