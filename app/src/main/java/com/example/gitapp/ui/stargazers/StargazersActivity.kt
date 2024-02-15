package com.example.gitapp.ui.stargazers

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gitapp.appComponent
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.databinding.ActivityStargazersBinding
import com.example.gitapp.injection.factories.PERIOD_KEY
import com.example.gitapp.injection.factories.STARGAZERS_KEY
import com.example.gitapp.injection.factories.StargazersAdapterFactory
import com.example.gitapp.injection.factories.StargazersPresenterFactory
import com.example.gitapp.ui.base.BaseActivity
import moxy.ktx.moxyPresenter
import java.util.ArrayList
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class StargazersActivity : BaseActivity(), StargazersView {

    private lateinit var binding: ActivityStargazersBinding

    @Inject
    lateinit var stargazersPresenterFactory: StargazersPresenterFactory.Factory

    private val stargazersPresenter: StargazersPresenter by moxyPresenter {
        val extras = intent.extras!!
        stargazersPresenterFactory.create(extras.getParcelableArrayList(STARGAZERS_KEY, ApiStarredData::class.java)!!)
            .createPresenter()
    }

    @Inject
    lateinit var stargazersAdapterFactory: StargazersAdapterFactory.Factory

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityStargazersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textPeriod.text = intent.extras?.getString(PERIOD_KEY) ?: ""
    }

    override fun initRecyclerView(stargazers: ArrayList<ApiStarredData>) {
        val adapter = stargazersAdapterFactory.create(stargazers).createStargazersAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerviewStargazers.layoutManager = layoutManager
        binding.recyclerviewStargazers.adapter = adapter
    }
}