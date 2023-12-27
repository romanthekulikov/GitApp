package com.example.gitapp.ui.diagram

import android.view.View
import com.example.gitapp.data.api.GitApiClient
import com.example.gitapp.data.api.entities.GitStarredEntity
import com.example.gitapp.ui.diagram.models.Weak
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date

//const val DAY = 0
//const val WEAK = 1
//const val YEAR = 2
@InjectViewState
class DiagramPresenter(private val repositoryName: String,
                       private val ownerName: String,
                       private val ownerIconUrl: String,
                       private val stargazersCount: Int) : MvpPresenter<DiagramView>() {
    init {
        getAllStarred()
        displayRepository()
    }

    private var currentPageItemsStarred: MutableList<GitStarredEntity> = mutableListOf()
    private var listPageItemsStarred: MutableList<GitStarredEntity> = mutableListOf()
    //private var diagramMode: Int = DAY
    private var wealList = mutableListOf<Weak>()
    private fun getAllStarred() {
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
                        splitPageItemsStarred()
                    }
                }

                override fun onFailure(call: Call<List<GitStarredEntity>>, t: Throwable) {
                    t.message?.let { viewState.showError(it) }
                }

            })
        }
    }

    private fun displayRepository() {
        viewState.displayRepositoryItem(name = repositoryName, ownerIconUrl = ownerIconUrl)
    }

    private fun splitPageItemsStarred() {
        var weak = Weak()
        var lastDay = 0
        val calendar: Calendar = Calendar.getInstance()
        for (pageIndex in 0..< listPageItemsStarred.size) {
            calendar.time = Date(listPageItemsStarred[pageIndex].getTime())
            val weakDay = calendar.get(Calendar.DAY_OF_WEEK)
            if (weakDay < lastDay) {
                wealList.add(weak)
                weak = Weak()
            }
            lastDay = weakDay
            when (weakDay) {
                Calendar.TUESDAY -> weak.tuesday.add(listPageItemsStarred[pageIndex])
                Calendar.MONDAY -> weak.monday.add(listPageItemsStarred[pageIndex])
                Calendar.WEDNESDAY -> weak.wednesday.add(listPageItemsStarred[pageIndex])
                Calendar.THURSDAY -> weak.thursday.add(listPageItemsStarred[pageIndex])
                Calendar.FRIDAY -> weak.friday.add(listPageItemsStarred[pageIndex])
                Calendar.SATURDAY -> weak.saturday.add(listPageItemsStarred[pageIndex])
                Calendar.SUNDAY -> {
                    weak.sunday.add(listPageItemsStarred[pageIndex])
                    wealList.add(weak)
                    weak = Weak()
                }
            }
        }
    }
}