package com.example.gitapp.ui.main.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitapp.data.api.entities.GitRepositoryEntity
import com.example.gitapp.databinding.ItemRepositoryBinding

class RepoAdapter(
    private val helper: RepoRecyclerHelper
) : PagedListAdapter<GitRepositoryEntity, RepoAdapter.RepositoryViewHolder>(DiffUtilsCallback.diffUtilsCallback) {
    object DiffUtilsCallback {
        val diffUtilsCallback = object : DiffUtil.ItemCallback<GitRepositoryEntity>() {
            override fun areItemsTheSame(oldItem: GitRepositoryEntity, newItem: GitRepositoryEntity): Boolean {
                return oldItem.repositoryName == newItem.repositoryName
            }

            override fun areContentsTheSame(oldItem: GitRepositoryEntity, newItem: GitRepositoryEntity): Boolean {
                return (oldItem.owner.name == newItem.owner.name) && (oldItem.owner.avatarUrl == newItem.owner.avatarUrl)
            }
        }
    }

    inner class RepositoryViewHolder(
        private val binding: ItemRepositoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GitRepositoryEntity) {
            Glide.with(this.itemView).load(item.owner.avatarUrl).circleCrop().into(binding.ownerIcon)
            binding.repositoryName.text = item.repositoryName
            binding.view.setOnClickListener {
                helper.onRepositoryClick(item)
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

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    interface RepoRecyclerHelper {
        fun onRepositoryClick(repository: GitRepositoryEntity)
    }
}

//class RepoAdapter(
//    private val repositoryList: List<GitRepositoryEntity>,
//    private val listener: RepositoryClickListener
//) : RecyclerView.Adapter<RepoAdapter.RepositoryViewHolder>() {
//
//    private var ownerIcon: Bitmap = Bitmap.createBitmap(30, 30, Bitmap.Config.ARGB_8888)
//
//    constructor(repositoryList: List<GitRepositoryEntity>,
//                listener: RepositoryClickListener,
//                ownerIcon: Bitmap
//    ) : this(repositoryList, listener) {
//        this.ownerIcon = ownerIcon
//    }
//
//    inner class RepositoryViewHolder(
//        private val binding: ItemRepositoryBinding
//    ) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: GitRepositoryEntity) {
//            binding.repositoryName.text = item.repositoryName
//            binding.ownerIcon.setImageBitmap(ownerIcon)
//            binding.view.setOnClickListener {
//                listener.onRepositoryClick(item)
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
//        val binding = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context))
//        val layoutParams = RecyclerView.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        binding.root.layoutParams = layoutParams
//        return RepositoryViewHolder(binding = binding)
//    }
//
//    override fun getItemCount(): Int {
//        return repositoryList.size
//    }
//
//    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
//        val item = repositoryList[position]
//        holder.bind(item)
//    }
//
//    interface RepositoryClickListener {
//        fun onRepositoryClick(repository: GitRepositoryEntity)
//    }
//}