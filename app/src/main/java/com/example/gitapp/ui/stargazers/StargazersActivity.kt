package com.example.gitapp.ui.stargazers

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.databinding.ActivityStargazersBinding

class StargazersActivity : AppCompatActivity() {
    object StargazersIntent {
        const val PERIOD_KEY = "period"
        const val STARGAZERS_KEY = "stargazers"

        fun createIntent(fromWhomContext: Context, period: String, stargazers: ArrayList<ApiStarredData>): Intent {
            val intent = Intent(fromWhomContext, StargazersActivity::class.java)
            intent.putExtra(PERIOD_KEY, period)
            intent.putParcelableArrayListExtra(STARGAZERS_KEY, stargazers)

            return intent
        }
    }

    private lateinit var binding: ActivityStargazersBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStargazersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textPeriod.text = intent.extras?.getString(StargazersIntent.PERIOD_KEY) ?: ""

        initRecyclerView()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initRecyclerView() {
        val stargazers =
            intent.extras?.getParcelableArrayList(StargazersIntent.STARGAZERS_KEY, ApiStarredData::class.java)
        val adapter = StargazersAdapter(stargazers!!)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerviewStargazers.layoutManager = layoutManager
        binding.recyclerviewStargazers.adapter = adapter
    }
}