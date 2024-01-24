package com.example.gitapp.ui.diagram

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.gitapp.databinding.ActivityDiagramBinding
import com.example.gitapp.ui.base.BaseActivity
import moxy.ktx.moxyPresenter

class DiagramActivity : BaseActivity(), DiagramView {
    private lateinit var binding: ActivityDiagramBinding
    private val diagramPresenter by moxyPresenter {
        val extras = intent.extras!!
        DiagramPresenter(
            repositoryName = extras.getString(repositoryNameKey, ""),
            ownerName = extras.getString(ownerNameKey, ""),
            ownerIconUrl = extras.getString(ownerIconUrlKey, ""),
            stargazersCount = extras.getInt(stargazersCountKey, 0)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagramBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progress.isActivated = true
    }

    override fun displayRepositoryItem(name: String, ownerIconUrl: String) {
        binding.repository.repoName.text = name
        Glide.with(this@DiagramActivity)
            .asBitmap()
            .circleCrop()
            .load(ownerIconUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.repository.ownerIcon.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    override fun changeVisibilityProgressBar(visibility: Int) {
        binding.progress.visibility = visibility
    }

    companion object DiagramIntent {
        const val repositoryNameKey = "repository_name_key"
        const val ownerNameKey = "owner_name_key"
        const val stargazersCountKey = "stargazers_count_key"
        const val ownerIconUrlKey = "repository_owner_icon_url_key"
        fun getIntent(
            fromWhomContext: Context,
            repositoryName: String,
            ownerName: String,
            ownerIconUrl: String,
            stargazersCount: Int
        ): Intent {
            val intent = Intent(fromWhomContext, DiagramActivity::class.java)
            intent.putExtra(repositoryNameKey, repositoryName)
            intent.putExtra(ownerNameKey, ownerName)
            intent.putExtra(stargazersCountKey, stargazersCount)
            intent.putExtra(ownerIconUrlKey, ownerIconUrl)

            return intent
        }
    }
}