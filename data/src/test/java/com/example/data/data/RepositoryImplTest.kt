package com.example.data.data

import com.example.data.data.api.GitApiService
import com.example.data.data.api.models.ApiRepo
import com.example.data.data.api.models.ApiUser
import io.mockk.every
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Response

class RepositoryImplTest {
    @Test
    fun getRepoFromApiTest() {
        val api = spyk<GitApiService>()
        every {
            runBlocking {
                api.fetchOwnerRepo("", "")
            }
        } returns Response.success(ApiRepo("name", ApiUser("user", "avatar"), 15))
        runBlocking {
            val repo = api.fetchOwnerRepo("", "")
        }
    }
}