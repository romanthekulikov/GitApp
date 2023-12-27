package com.example.gitapp.ui.main

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.gitapp.data.api.entities.GitRepositoryEntity
import com.example.gitapp.databinding.ActivityMainBinding
import com.example.gitapp.ui.base.BaseMvpActivity
import com.example.gitapp.ui.diagram.DiagramActivity
import moxy.ktx.moxyPresenter

class MainActivity : BaseMvpActivity(), MainView,
    RepoAdapter.RepositoryClickListener {
    private lateinit var binding: ActivityMainBinding

    private val repositoriesPresenter by moxyPresenter { MainPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.repositories.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.progress.isActivated = true
        binding.enter.setOnClickListener {
            val ownerName = binding.ownerEditText.text.toString()
            repositoriesPresenter.requestGetRepositories(ownerName = ownerName)
        }
    }

    override fun showRepositories(listRepos: List<GitRepositoryEntity>) {
        val ownerIconUrl = listRepos[0].owner.avatarUrl
        Glide.with(this@MainActivity)
            .asBitmap()
            .load(ownerIconUrl)
            .circleCrop()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val adapter = RepoAdapter(repositoryList = listRepos, ownerIcon = resource, listener = this@MainActivity)
                    binding.repositories.adapter = adapter
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    val adapter = RepoAdapter(repositoryList = listRepos, listener = this@MainActivity)
                    binding.repositories.adapter = adapter

                }
            })
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
}