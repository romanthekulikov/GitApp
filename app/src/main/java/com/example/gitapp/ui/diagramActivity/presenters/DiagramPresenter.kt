package com.example.gitapp.ui.diagramActivity.presenters

import android.os.Bundle
import android.view.View
import com.example.gitapp.retrofit.GitApiClient
import com.example.gitapp.retrofit.entities.GitStarredEntity
import com.example.gitapp.ui.IntentKeys
import com.example.gitapp.ui.diagramActivity.views.DiagramView
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

const val DAY = 0
const val WEAK = 1
const val YEAR = 2
@InjectViewState
class DiagramPresenter : MvpPresenter<DiagramView>() {
    private var ownerName = ""
    private var repositoryName = ""
    private var currentPageItemsStarred: MutableList<GitStarredEntity> = mutableListOf()
    private var listPageItemsStarred: MutableList<GitStarredEntity> = mutableListOf()
    private var stargazersCount = 0
    private var diagramMode: Int = DAY
    fun getAllStarred(extras: Bundle) {
        ownerName = extras.getString(IntentKeys.ownerName, "")
        repositoryName = extras.getString(IntentKeys.repositoryName, "")
        stargazersCount = extras.getInt(IntentKeys.stargazersCount, 0)
        val pageCount: Int = (stargazersCount / 100) + 1
        viewState.changeVisibilityProgressBar(View.VISIBLE)
        for (page in 0..pageCount) {
            val client = GitApiClient
                .apiService
                .fetchRepositoriesStarred(ownerName = ownerName, repository = repositoryName, itemInPageCount = 100, page = page)

            client.enqueue(object : Callback<List<GitStarredEntity>> {
                override fun onResponse(call: Call<List<GitStarredEntity>>, response: Response<List<GitStarredEntity>>) {
                    currentPageItemsStarred = response.body()?.toMutableList() ?: mutableListOf()
                    listPageItemsStarred.addAll(currentPageItemsStarred)
                    if (page == pageCount) {
                        viewState.changeVisibilityProgressBar(View.GONE)

                    }
                }

                override fun onFailure(call: Call<List<GitStarredEntity>>, t: Throwable) {
                    t.message?.let { viewState.showError(it) }
                }

            })
        }
    }
}