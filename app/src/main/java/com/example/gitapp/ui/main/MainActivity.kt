package com.example.gitapp.ui.main

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.databinding.ActivityMainBinding
import com.example.gitapp.ui.base.BaseActivity
import com.example.gitapp.ui.diagram.DiagramActivity
import com.omega_r.libs.omegarecyclerview.pagination.OnPageRequestListener
import moxy.ktx.moxyPresenter

//import java.time.DayOfWeek
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//import java.util.Locale


//class Main : Application() {
//    lateinit var appComponent: AppComponent
//    override fun onCreate() {
//        super.onCreate()
//            appComponent = DaggerAppComp
//    }
//}

class MainActivity : BaseActivity(), MainView,
    RepoAdapter.RepoRecyclerHelper, OnPageRequestListener {
    private lateinit var binding: ActivityMainBinding

    //@Inject
    private lateinit var repoAdapter: RepoAdapter
    private var ownerName = ""
    private var pageRepo = 0

    //@Inject
    private val mainPresenter by moxyPresenter { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.repositories.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.ownerEditText.setOnEnterClickListener {
            showRepo()
        }
        binding.enter.setOnClickListener {
            showRepo()
        }

        initRecyclerView()
    }

    private fun android.widget.EditText.setOnEnterClickListener(action: () -> Unit) {
        this.setOnKeyListener { _, keyCode, event ->
            if (event != null) {
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    action()
                    this.clearFocus()
                    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
                }
            }

            false
        }
    }

    private fun showRepo() {
        val currentOwnerName = binding.ownerEditText.text.toString()
        if (currentOwnerName != ownerName) {
            binding.stub.visibility = View.GONE
            binding.repositories.visibility = View.VISIBLE
            ownerName = currentOwnerName
            repoAdapter.clear()
            binding.repositories.resetPagination()
            binding.repositories.showProgressPagination()
        }
    }

    private fun initRecyclerView() {
        val adapter = RepoAdapter(this@MainActivity)
        repoAdapter = adapter
        binding.repositories.setPaginationCallback(this)
        binding.repositories.adapter = adapter
        binding.repositories.visibility = View.VISIBLE
        binding.stub.visibility = View.GONE
    }

    override fun showRepositories(listRepo: List<ApiRepo>) {
        if (listRepo.size < 100) {
            binding.repositories.hidePagination()
        }
        repoAdapter.addValues(listRepo)
    }

    override fun showRecyclerError() {
        binding.repositories.showErrorPagination()
    }

    override fun showEmptyNotificationList() {
        binding.repositories.visibility = View.GONE
        binding.stub.visibility = View.VISIBLE
        binding.repositories.hidePagination()
    }

    override fun onRepoClicked(repo: ApiRepo) {
        val intent = DiagramActivity.getIntent(
            fromWhomContext = this@MainActivity,
            repositoryName = repo.name,
            ownerName = repo.owner.name,
            ownerIconUrl = repo.owner.avatarUrl,
            stargazersCount = repo.stargazersCount
        )

        startActivity(intent)
    }

    override fun onRetryClicked() {
        binding.repositories.showProgressPagination()
        mainPresenter.requestGetRepo(ownerName = ownerName, page = pageRepo)
    }

    override fun onPageRequest(page: Int) {
        pageRepo = page
        if (page > 0) { //Zero and first page are equal
            mainPresenter.requestGetRepo(ownerName = ownerName, page = pageRepo)
        }
    }

    override fun getPagePreventionForEnd(): Int {
        return 10
    }
}