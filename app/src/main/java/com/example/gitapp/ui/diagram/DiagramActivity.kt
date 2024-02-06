package com.example.gitapp.ui.diagram

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.gitapp.R
import com.example.gitapp.appComponent
import com.example.gitapp.data.PeriodType
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.databinding.ActivityDiagramBinding
import com.example.gitapp.injection.factories.DiagramPresenterFactory
import com.example.gitapp.injection.factories.OWNER_ICON_URL_KEY
import com.example.gitapp.injection.factories.OWNER_NAME_KAY
import com.example.gitapp.injection.factories.REPO_NAME_KEY
import com.example.gitapp.injection.factories.STARGAZERS_COUNT_KEY
import com.example.gitapp.injection.factories.StargazerIntentFactory
import com.example.gitapp.ui.base.BaseActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.tabs.TabLayout
import moxy.ktx.moxyPresenter
import javax.inject.Inject

const val MONTH_TAB_POSITION = 1
const val YEAR_TAB_POSITION = 2

class DiagramActivity : BaseActivity(), DiagramView {

    private lateinit var binding: ActivityDiagramBinding
    private lateinit var histogramView: BarChart

    @Inject
    lateinit var stargazerIntentFactory: StargazerIntentFactory.Factory

    @Inject
    lateinit var diagramPresenterFactory: DiagramPresenterFactory.Factory

    private val diagramPresenter: DiagramPresenter by moxyPresenter {
        val extras = intent.extras!!
        diagramPresenterFactory.create(
            repositoryName = extras.getString(REPO_NAME_KEY, ""),
            ownerName = extras.getString(OWNER_NAME_KAY, ""),
            ownerIconUrl = extras.getString(OWNER_ICON_URL_KEY, ""),
            stargazersCount = extras.getInt(STARGAZERS_COUNT_KEY, 0),
            appComponent = appComponent // looks like something bad...
        ).createPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityDiagramBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressbar.isActivated = true
        binding.imageNextButton.setOnClickListener {
            diagramPresenter.requestMoveToNextHistogramPage()
        }
        binding.imagePreviousButton.setOnClickListener {
            diagramPresenter.requestMoveToPreviousHistogramPage()
        }

        diagramInit()
        periodTabInit()
    }

    private fun diagramInit() {
        histogramView = binding.barChartHistogram
        histogramView.isScaleYEnabled = false
        histogramView.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                try {
                    val stargazersData = diagramPresenter.requestPartPeriodData(e!!.x.toInt())
                    val intent = stargazerIntentFactory.create(
                        fromWhomContext = this@DiagramActivity,
                        stargazers = stargazersData.first as ArrayList<ApiStarredData>,
                        period = stargazersData.second
                    ).createIntent()

                    startActivity(intent)
                } catch (e: IndexOutOfBoundsException) {
                    // nothing
                }
            }

            override fun onNothingSelected() {}
        })
    }

    private fun periodTabInit() {
        binding.tabLayoutPeriod.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    MONTH_TAB_POSITION -> diagramPresenter.requestChangeDiagramMode(PeriodType.MONTH)
                    YEAR_TAB_POSITION -> diagramPresenter.requestChangeDiagramMode(PeriodType.YEAR)
                    else -> diagramPresenter.requestChangeDiagramMode(PeriodType.WEEK)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun displayRepositoryItem(name: String, ownerIconUrl: String) {
        binding.repository.textRepoName.text = name
        Glide.with(this@DiagramActivity)
            .asBitmap()
            .circleCrop()
            .load(ownerIconUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.repository.imageOwner.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    override fun displayData(data: BarData, valueFormatter: ValueFormatter) {
        histogramView.data = data
        histogramView.notifyDataSetChanged()
        histogramView.invalidate()
        histogramView.xAxis.valueFormatter = valueFormatter
        histogramView.isActivated = true
    }

    override fun setPreviousButtonEnabled(isEnabled: Boolean) {
        binding.imagePreviousButton.isEnabled = isEnabled
        if (isEnabled) {
            binding.imagePreviousButton.setImageResource(R.drawable.ic_arrow_enabled)
        } else {
            binding.imagePreviousButton.setImageResource(R.drawable.ic_arrow_disabled)
        }
    }

    override fun setNextButtonEnabled(isEnabled: Boolean) {
        binding.imageNextButton.isEnabled = isEnabled
        if (isEnabled) {
            binding.imageNextButton.setImageResource(R.drawable.ic_arrow_enabled)
        } else {
            binding.imageNextButton.setImageResource(R.drawable.ic_arrow_disabled)
        }
    }

    override fun changeVisibilityProgressBar(visibility: Int) {
        binding.layoutProgressbar.visibility = visibility
    }
}