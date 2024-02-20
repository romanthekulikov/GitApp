package com.example.gitapp.data.api

import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.data.api.models.ApiStargazer
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val ITEM_PER_STARGAZERS_PAGE = 100
const val ITEM_PER_REPOS_PAGE = 100

interface GitApiService {
    @GET("/users/{user}/repos")
    suspend fun fetchOwnerRepos(
        @Path(value = "user") ownerName: String,
        @Query(value = "page") numberOfPage: Int,
        @Query(value = "per_page") numberOfItemInPage: Int = ITEM_PER_REPOS_PAGE
    ): List<ApiRepo>

    @Headers("Accept: application/vnd.github.v3.star+json")
    @GET("repos/{user}/{repos}/stargazers")
    suspend fun fetchRepoStarred(
        @Path(value = "user") ownerName: String,
        @Path(value = "repos") repository: String,
        @Query(value = "per_page") itemInPageCount: Int = ITEM_PER_STARGAZERS_PAGE,
        @Query(value = "page") page: Int
    ): List<ApiStargazer>
}