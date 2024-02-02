package com.example.gitapp.ui.stargazers

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitapp.appComponent
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.databinding.ActivityStargazersBinding
import com.example.gitapp.injection.factories.PERIOD_KEY
import com.example.gitapp.injection.factories.STARGAZERS_KEY
import com.example.gitapp.injection.factories.StargazersAdapterFactory
import javax.inject.Inject

class StargazersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStargazersBinding

    @Inject
    lateinit var stargazersAdapterFactory: StargazersAdapterFactory.Factory

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityStargazersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textPeriod.text = intent.extras?.getString(PERIOD_KEY) ?: ""

        initRecyclerView()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initRecyclerView() {
        val stargazers =
            intent.extras?.getParcelableArrayList(STARGAZERS_KEY, ApiStarredData::class.java)
        val adapter = stargazersAdapterFactory.create(stargazers!!).createStargazersAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerviewStargazers.layoutManager = layoutManager
        binding.recyclerviewStargazers.adapter = adapter
    }
}