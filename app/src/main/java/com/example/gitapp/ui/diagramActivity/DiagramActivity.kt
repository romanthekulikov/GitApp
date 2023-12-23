package com.example.gitapp.ui.diagramActivity

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import com.example.gitapp.databinding.ActivityDiagramBinding
import com.example.gitapp.ui.diagramActivity.presenters.DiagramPresenter
import com.example.gitapp.ui.diagramActivity.presenters.RepositoryItemPresenter
import com.example.gitapp.ui.diagramActivity.views.DiagramView
import com.example.gitapp.ui.diagramActivity.views.RepositoryItemView
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.lang.NullPointerException

class DiagramActivity : MvpAppCompatActivity(), RepositoryItemView, DiagramView {
    private lateinit var binding: ActivityDiagramBinding
    private val repositoryItemPresenter by moxyPresenter { RepositoryItemPresenter() }
    private val diagramPresenter by moxyPresenter { DiagramPresenter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagramBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progress.isActivated = true
        try {
            repositoryItemPresenter.displayRepository(extras = intent.extras!!, context = this@DiagramActivity)
            diagramPresenter.getAllStarred(extras = intent.extras!!)
        } catch (_: NullPointerException) {}

    }

    override fun displayRepositoryItem(name: String, ownerIcon: Bitmap) {
        binding.repository.repositoryName.text = name
        binding.repository.ownerIcon.setImageBitmap(ownerIcon)
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun changeVisibilityProgressBar(visibility: Int) {
        binding.progress.visibility = visibility
    }
}