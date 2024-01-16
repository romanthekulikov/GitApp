package com.example.gitapp.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitapp.data.api.entities.GitRepositoryEntity
import com.example.gitapp.databinding.ActivityMainBinding
import com.example.gitapp.ui.base.BaseMvpActivity
import com.example.gitapp.ui.diagram.DiagramActivity
import com.example.gitapp.ui.main.paging.RepoAdapter
import moxy.ktx.moxyPresenter


class MainActivity : BaseMvpActivity(), MainView,
    RepoAdapter.RepoRecyclerHelper {
    private lateinit var binding: ActivityMainBinding

    private val repositoriesPresenter by moxyPresenter { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.repositories.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.progress.isActivated = true
        binding.ownerEditText.onEnterClick {
            showRepo()
        }
        binding.enter.setOnClickListener {
            showRepo()
        }
    }

    private fun showRepo() {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ownerName = binding.ownerEditText.text.toString()
        repositoriesPresenter.requestGetRepositories(connectivityManager = connectivityManager, ownerName = ownerName)
    }

    override fun showRepositories(pagedListLiveData: LiveData<PagedList<GitRepositoryEntity>>) {
        binding.repositories.visibility = View.VISIBLE
        binding.stub.visibility = View.GONE
        val adapter = RepoAdapter(this)

        pagedListLiveData.observe(this) { repos ->
            adapter.submitList(repos)
        }

        binding.repositories.adapter = adapter
    }

    override fun showEmptyListRepo() {
        binding.repositories.visibility = View.GONE
        binding.stub.visibility = View.VISIBLE
    }

    override fun changeVisibilityProgressBar(visibility: Int) {
        binding.progress.visibility = visibility
    }

    override fun onRepositoryClick(repository: GitRepositoryEntity) {
        val intent = DiagramActivity.getIntent(
            fromWhomContext = this@MainActivity,
            repositoryName = repository.repositoryName,
            ownerName = repository.owner.name,
            repositoryOwnerIconUrl = repository.owner.avatarUrl,
            stargazersCount = repository.stargazersCount
        )

        startActivity(intent)
    }

    private fun android.widget.EditText.onEnterClick(action: () -> Unit) {
        this.setOnKeyListener { _, keyCode, event ->
            if (event != null) {
                if ((event.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    action()
                }
            }
            false
        }
    }
}