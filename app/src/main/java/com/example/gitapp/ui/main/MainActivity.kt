package com.example.gitapp.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitapp.databinding.ActivityMainBinding
import com.example.gitapp.ui.base.BaseActivity
import com.example.gitapp.ui.diagram.DiagramActivity
import com.example.gitapp.ui.diagram.IS_FAVORITE_INTENT_KEY
import com.omega_r.libs.omegarecyclerview.pagination.OnPageRequestListener
import moxy.ktx.moxyPresenter

class MainActivity : BaseActivity(), MainView, RepoAdapter.RepoRecyclerCallback, OnPageRequestListener {

    private val mainPresenter: MainPresenter by moxyPresenter { MainPresenter() }
    private lateinit var binding: ActivityMainBinding
    private lateinit var repoAdapter: RepoAdapter
    private lateinit var selectedRepo: com.example.domain.domain.models.RepoEntity
    private var selectedRepoPosition = 0
    private var nextLoadPage = 0
    private var needNewPage = false
    private var ownerName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.repositories.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.inputOwner.setOnEnterClickListener {
            showRepo()
        }
        binding.imageEnter.setOnClickListener {
            showRepo()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }

        initRecyclerView()
    }

    private fun showRepo() {
        val currentOwnerName = binding.inputOwner.text.toString()
        if (currentOwnerName != ownerName) {
            binding.textStub.visibility = View.GONE
            binding.repositories.visibility = View.VISIBLE
            ownerName = currentOwnerName
            repoAdapter.clear()
            binding.repositories.resetPagination()
            binding.repositories.showProgressPagination()
        }
    }

    @SuppressLint("InlinedApi")
    private fun checkNotificationPermission() {
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            //RepoWorkerHelper.initWorker(this, startAfterSec = 15)
        }
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        when (it) {
            true -> {
                //RepoWorkerHelper.initWorker(this@MainActivity, startAfterSec = 15)
            }

            false -> Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_LONG).show()
        }
    }

    private fun initRecyclerView() {
        val adapter = RepoAdapter(this)
        repoAdapter = adapter
        binding.repositories.setPaginationCallback(this)
        binding.repositories.adapter = adapter
        binding.repositories.visibility = View.VISIBLE
        binding.textStub.visibility = View.GONE
    }

    override fun showLoadedRepositories(listRepo: List<com.example.domain.domain.models.RepoEntity>) {
        if (listRepo.size < 100) {
            binding.repositories.hidePagination()
        }
        val pageAdditionSuccess = repoAdapter.addValue(listRepo)
        if (!pageAdditionSuccess) {
            mainPresenter.requestGetRepo(ownerName = ownerName, page = nextLoadPage, showErrorOnFailLoad = false)
        }

        nextLoadPage++
    }

    override fun showRecyclerError() {
        binding.repositories.showErrorPagination()
    }

    override fun showEmptyNotificationList() {
        binding.textStub.visibility = View.VISIBLE
        binding.repositories.showErrorPagination()
        binding.repositories.hidePagination()
    }

    override fun onChangeRepoFavorite(repo: com.example.domain.domain.models.RepoEntity, isFavorite: Boolean, position: Int) {
        mainPresenter.requestChangeFavoriteRepo(repo, isFavorite)
        repoAdapter.notifyItemChanged(position)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRepoClickListener(repo: com.example.domain.domain.models.RepoEntity, position: Int) {
        selectedRepo = repo
        selectedRepoPosition = position
        val intent = DiagramActivity.get(fromWhomContext = this, repo = selectedRepo)

        diagramActivityResultLauncher.launch(intent)
    }

    private val diagramActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val repoIsFavorite = intent?.getBooleanExtra(IS_FAVORITE_INTENT_KEY, false)!!
                onChangeRepoFavorite(selectedRepo, repoIsFavorite, selectedRepoPosition)
                repoAdapter.changeFavoriteValue(selectedRepoPosition, repoIsFavorite)
            }
        }

    override fun onRetryClickListener() {
        binding.textStub.visibility = View.GONE
        binding.repositories.showProgressPagination()
    }

    override fun onPageRequest(page: Int) {
        needNewPage = true
        if (page > 0) { // Zero and first page are equal
            mainPresenter.requestGetRepo(
                ownerName = ownerName,
                page = nextLoadPage,
                requireNewPage = true,
                showErrorOnFailLoad = true
            )
        }
    }

    override fun getPagePreventionForEnd(): Int {
        return 10
    }

    private fun android.widget.EditText.setOnEnterClickListener(action: () -> Unit) {
        this.setOnKeyListener { _, keyCode, event ->
            if (event != null) {
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    action()
                    this.clearFocus()
                    val inputMethodManager =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
                }
            }

            false
        }
    }
}