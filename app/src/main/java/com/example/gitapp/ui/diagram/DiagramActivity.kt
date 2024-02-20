package com.example.gitapp.ui.diagram

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.gitapp.R
import com.example.gitapp.appComponent
import com.example.gitapp.data.database.entity.RepoEntity
import com.example.gitapp.databinding.ActivityDiagramBinding
import com.example.gitapp.entity.Stared
import com.example.gitapp.injection.factories.DiagramPresenterFactory
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

const val IS_FAVORITE_INTENT_KEY = "is_favorite"
const val REPO_KEY = "repo"
@Suppress("UNCHECKED_CAST")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            repo = extras.getParcelable(REPO_KEY, RepoEntity::class.java)!!,
            appComponent = appComponent // looks like something bad...
        ).createPresenter()
    }

    private val onBackPressed = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent()
            intent.putExtra(IS_FAVORITE_INTENT_KEY, binding.repo.checkboxStar.isChecked)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
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

        onBackPressedDispatcher.addCallback(onBackPressed)
    }

    private fun diagramInit() {
        histogramView = binding.barChartHistogram
        histogramView.isScaleYEnabled = false
        histogramView.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry?, h: Highlight?) {
                try {
                    val periodDataTime = diagramPresenter.requestPeriodDataTime(entry?.data as List<Stared>)
                    val intent = stargazerIntentFactory.create(
                        fromWhomContext = this@DiagramActivity,
                        stargazers = entry.data as List<Stared>,
                        period = periodDataTime
                    ).createIntent()

                    startActivity(intent)
                } catch (e: IndexOutOfBoundsException) {
                    // nothing
                }
            }

            override fun onNothingSelected() { /**nothing**/ }
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

            override fun onTabUnselected(tab: TabLayout.Tab?) { /**nothing**/ }
            override fun onTabReselected(tab: TabLayout.Tab?) { /**nothing**/ }
        })
    }

    override fun displayRepositoryItem(name: String, ownerIconUrl: String, isFavorite: Boolean) {
        binding.repo.textRepoName.text = name
        binding.repo.checkboxStar.isChecked = isFavorite
        Glide.with(this@DiagramActivity)
            .asBitmap()
            .circleCrop()
            .load(ownerIconUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.repo.imageOwner.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) { /**nothing**/ }
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