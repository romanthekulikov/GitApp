package com.example.gitapp.ui.stargazers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.databinding.ActivityStargazersBinding

class StargazersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStargazersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStargazersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.period.text = intent.extras?.getString(StargazersIntent.periodKey) ?: ""

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val stargazers = intent.extras?.getParcelableArrayList(StargazersIntent.stargazersKey, ApiStarredData::class.java)
        val adapter = StargazersAdapter(stargazers!!)
        val layoutManager = LinearLayoutManager(this)
        binding.stargazers.layoutManager = layoutManager
        binding.stargazers.adapter = adapter
    }

    object StargazersIntent {
        const val periodKey = "period"
        const val stargazersKey = "stargazers"

        fun getIntent(fromWhomContext: Context, period: String, stargazers: ArrayList<ApiStarredData>): Intent {
            val intent = Intent(fromWhomContext, StargazersActivity::class.java)
            intent.putExtra(periodKey, period)
            intent.putParcelableArrayListExtra(stargazersKey, stargazers)

            return intent
        }
    }
}