package com.example.gitapp.ui.stargazers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.gitapp.databinding.ActivityStargazersBinding
import com.example.gitapp.ui.base.BaseActivity
import com.example.gitapp.ui.diagram.models.Period
import moxy.ktx.moxyPresenter

class StargazersActivity : BaseActivity() {
    private lateinit var binding: ActivityStargazersBinding
    private val stargazersPresenter by moxyPresenter { StargazersPresenter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStargazersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    object StargazerIntent {
        const val periodKey = "period"
        const val stargazersKey = "stargazers"

        fun getIntent(fromWhomContext: Context, period: String, stargazers: Period): Intent {
            val intent = Intent(fromWhomContext, StargazersActivity::class.java)
            intent.putExtra(periodKey, period)
            intent.putExtra(stargazersKey, stargazers)

            return intent
        }
    }
}