package com.example.gitapp.ui.main

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitapp.appComponent
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.databinding.ActivityMainBinding
import com.example.gitapp.injection.factories.DiagramIntentFactory
import com.example.gitapp.injection.factories.RepoAdapterFactory
import com.example.gitapp.ui.base.BaseActivity
import com.example.gitapp.ui.diagram.IS_FAVORITE_INTENT_KEY
import com.omega_r.libs.omegarecyclerview.pagination.OnPageRequestListener
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class MainActivity : BaseActivity(), MainView,
    RepoAdapter.RepoRecyclerCallback, OnPageRequestListener {

    @Inject
    lateinit var repoAdapterFactory: RepoAdapterFactory.Factory

    @Inject
    lateinit var diagramIntent: DiagramIntentFactory.Factory

    @Inject
    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    private lateinit var binding: ActivityMainBinding
    private lateinit var repoAdapter: RepoAdapter
    private lateinit var selectedRepo: ApiRepo
    private var selectedRepoPosition = 0

    @ProvidePresenter
    fun provide(): MainPresenter = mainPresenter
    private var ownerName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
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

    private fun initRecyclerView() {
        val adapter = repoAdapterFactory.create(this).createRepoAdapter()
        repoAdapter = adapter
        binding.repositories.setPaginationCallback(this)
        binding.repositories.adapter = adapter
        binding.repositories.visibility = View.VISIBLE
        binding.textStub.visibility = View.GONE
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
        binding.textStub.visibility = View.VISIBLE
        binding.repositories.showErrorPagination()
        binding.repositories.hidePagination()
    }

    override fun onChangeRepoFavorite(repo: ApiRepo, isFavorite: Boolean, position: Int) {
        mainPresenter.requestChangeFavoriteRepo(repo, isFavorite)
        repoAdapter.notifyItemChanged(position)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRepoClicked(repo: ApiRepo, position: Int) {
        selectedRepo = repo
        selectedRepoPosition = position
        val intent = diagramIntent.create(
            fromWhomContext = this@MainActivity,
            selectedRepo
        ).createIntent()

        startActivityForResult.launch(intent)
    }

    private val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val repoIsFavorite = intent?.getBooleanExtra(IS_FAVORITE_INTENT_KEY, false)!!
            onChangeRepoFavorite(selectedRepo, repoIsFavorite, selectedRepoPosition)
            repoAdapter.changeFavoriteValue(selectedRepoPosition, repoIsFavorite)
        }
    }

    override fun onRetryClicked() {
        binding.textStub.visibility = View.GONE
        binding.repositories.showProgressPagination()
    }

    override fun onPageRequest(page: Int) {
        if (page > 0) { //Zero and first page are equal
            mainPresenter.requestGetRepo(ownerName = ownerName, page = page)
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