package com.example.gitapp.main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitapp.databinding.ActivityMainBinding
import com.example.gitapp.main.adapters.RepositoriesAdapter
import com.example.gitapp.main.presenters.GetRepositoriesPresenter
import com.example.gitapp.main.views.OwnerEditTextView
import com.example.gitapp.retrofit.GitRepositoryResponse
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter

class MainActivity : MvpAppCompatActivity(), OwnerEditTextView,
    RepositoriesAdapter.RepositoryClickListener {
    private lateinit var binding: ActivityMainBinding

    private val repositoriesPresenter by moxyPresenter { GetRepositoriesPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.progress.isActivated = true
        binding.enter.setOnClickListener {
            val ownerName = binding.ownerEditText.text.toString()
            repositoriesPresenter.getRepositories(ownerName = ownerName, listener = this@MainActivity, context = this)
        }
    }

    override fun showRepositories(listRepos: List<GitRepositoryResponse>, adapter: RepositoriesAdapter) {
        binding.repositories.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.repositories.adapter = adapter
    }

    override fun changeVisibilityProgressBar(visibility: Int) {
        binding.progress.visibility = visibility
    }

    override fun showError(errorMsg: String) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show()
    }

    override fun onRepositoryClick(repository: GitRepositoryResponse) {

    }
}