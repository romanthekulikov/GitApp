package com.example.gitapp.ui.main

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapp.databinding.ItemRepositoryBinding
import com.example.gitapp.data.api.entities.GitRepositoryEntity

class RepoAdapter(
    private val repositoryList: List<GitRepositoryEntity>,
    private val listener: RepositoryClickListener
) : RecyclerView.Adapter<RepoAdapter.RepositoryViewHolder>() {

    private var ownerIcon: Bitmap = Bitmap.createBitmap(30, 30, Bitmap.Config.ARGB_8888)

    constructor(repositoryList: List<GitRepositoryEntity>,
                listener: RepositoryClickListener,
                ownerIcon: Bitmap
    ) : this(repositoryList, listener) {
        this.ownerIcon = ownerIcon
    }

    inner class RepositoryViewHolder(
        private val binding: ItemRepositoryBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GitRepositoryEntity) {
            binding.repositoryName.text = item.repositoryName
            binding.ownerIcon.setImageBitmap(ownerIcon)
            binding.view.setOnClickListener {
                listener.onRepositoryClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context))
        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
        return RepositoryViewHolder(binding = binding)
    }

    override fun getItemCount(): Int {
        return repositoryList.size
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        val item = repositoryList[position]
        holder.bind(item)
    }

    interface RepositoryClickListener {
        fun onRepositoryClick(repository: GitRepositoryEntity)
    }
}