package com.example.gitapp.ui.stargazers

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.domain.models.StaredModel
import com.example.domain.domain.entity.Stared
import com.example.gitapp.databinding.ActivityStargazersBinding
import com.example.gitapp.ui.base.BaseActivity
import moxy.ktx.moxyPresenter

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class StargazersActivity : BaseActivity(), StargazersView {

    private lateinit var binding: ActivityStargazersBinding

    private val stargazersPresenter: StargazersPresenter by moxyPresenter {
        val extras = intent.extras!!
        StargazersPresenter(extras.getParcelableArrayList(STARGAZERS_KEY, StaredModel::class.java)!!)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStargazersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textPeriod.text = intent.extras?.getString(PERIOD_KEY) ?: ""
    }

    override fun initRecyclerView(stargazers: List<com.example.domain.domain.entity.Stargazer>) {
        val adapter = StargazersAdapter(stargazers)
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerviewStargazers.layoutManager = layoutManager
        binding.recyclerviewStargazers.adapter = adapter
    }

    companion object StargazerIntent {

        const val PERIOD_KEY = "period"
        const val STARGAZERS_KEY = "stargazers"
        fun get(fromWhomContext: Context, period: String, stargazers: List<Stared>): Intent {
            val intent = Intent(fromWhomContext, StargazersActivity::class.java)
            intent.putExtra(PERIOD_KEY, period)
            intent.putParcelableArrayListExtra(STARGAZERS_KEY, java.util.ArrayList(stargazers))

            return intent
        }
    }
}