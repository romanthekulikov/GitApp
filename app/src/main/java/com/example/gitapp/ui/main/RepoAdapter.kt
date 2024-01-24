package com.example.gitapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitapp.data.api.entities.ApiRepo
import com.example.gitapp.databinding.ItemErrorProgressBinding
import com.example.gitapp.databinding.ItemProgressBinding
import com.example.gitapp.databinding.ItemRepoBinding
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.PaginationViewCreator


class RepoAdapter(private val helper: RepoRecyclerHelper) : OmegaRecyclerView.Adapter<RepoAdapter.RepoViewHolder>(),
    PaginationViewCreator {
    private val listRepo = mutableListOf<ApiRepo>()
    inner class RepoViewHolder(
        private val binding: ItemRepoBinding
    ) : OmegaRecyclerView.ViewHolder(binding.root) {
        fun bind(item: ApiRepo) {
            Glide.with(this.itemView).load(item.owner.avatarUrl).circleCrop().into(binding.ownerIcon)
            binding.repoName.text = item.name
            binding.view.setOnClickListener {
                helper.onRepoClicked(item)
            }
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context))
        val layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        binding.root.layoutParams = layoutParams
        return RepoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listRepo.size
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(listRepo[position])
    }

    override fun createPaginationView(parent: ViewGroup?, inflater: LayoutInflater?): View {
        return ItemProgressBinding.inflate(inflater!!, parent, false).root
    }

    override fun createPaginationErrorView(parent: ViewGroup?, inflater: LayoutInflater?): View {
        val binding = ItemErrorProgressBinding.inflate(inflater!!, parent, false)
        binding.againButton.setOnClickListener {
            helper.onRetryClicked()
        }
        return binding.root
    }

    fun addValues(listNewRepo: List<ApiRepo>) {
        listRepo.addAll(listNewRepo)
        notifyItemInserted(listRepo.size - listNewRepo.size)
    }

    fun clear() {
        val size = listRepo.size
        listRepo.clear()
        notifyItemRangeRemoved(0, size)
    }

    interface RepoRecyclerHelper {
        fun onRepoClicked(repo: ApiRepo)
        fun onRetryClicked()
    }
}