package com.example.gitapp.ui.mainActivity.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gitapp.databinding.ItemRepositoryBinding
import com.example.gitapp.retrofit.entities.GitRepositoryEntity

class RepositoriesAdapter(
    private val repositoryList: List<GitRepositoryEntity>,
    private val ownerImage: Bitmap,
    private val listener: RepositoryClickListener
) :
    RecyclerView.Adapter<RepositoriesAdapter.RepositoryViewHolder>() {

    inner class RepositoryViewHolder(
        private val binding: ItemRepositoryBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GitRepositoryEntity) {
            binding.repositoryName.text = item.repositoryName
            binding.ownerIcon.setImageBitmap(ownerImage)
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