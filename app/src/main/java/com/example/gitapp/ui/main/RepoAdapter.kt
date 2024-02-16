package com.example.gitapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitapp.data.api.models.ApiRepo
import com.example.gitapp.databinding.ItemErrorProgressBinding
import com.example.gitapp.databinding.ItemProgressBinding
import com.example.gitapp.databinding.ItemRepoBinding
import com.omega_r.libs.omegarecyclerview.OmegaRecyclerView
import com.omega_r.libs.omegarecyclerview.pagination.PaginationViewCreator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class RepoAdapter(
    private val helper: RepoRecyclerCallback
) : OmegaRecyclerView.Adapter<RepoAdapter.RepoViewHolder>(), PaginationViewCreator {

    private val repoList = mutableListOf<ApiRepo>()

    init {
        setHasStableIds(true)
    }

    override fun getItemCount(): Int {
        return repoList.size
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

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repoList[position], position)
    }

    override fun createPaginationView(parent: ViewGroup?, inflater: LayoutInflater?): View {
        return ItemProgressBinding.inflate(inflater!!, parent, false).root
    }

    override fun createPaginationErrorView(parent: ViewGroup?, inflater: LayoutInflater?): View {
        val binding = ItemErrorProgressBinding.inflate(inflater!!, parent, false)
        binding.buttonAgain.setOnClickListener {
            helper.onRetryClicked()
        }
        return binding.root
    }

    fun addValues(listNewRepo: List<ApiRepo>) {
        repoList.addAll(listNewRepo)
        notifyItemInserted(repoList.size - listNewRepo.size)
    }

    fun changeFavoriteValue(position: Int, isFavorite: Boolean) {
        repoList[position].isFavorite = isFavorite
    }

    fun clear() {
        val size = repoList.size
        repoList.clear()
        notifyItemRangeRemoved(0, size)
    }

    inner class RepoViewHolder(
        private val binding: ItemRepoBinding
    ) : OmegaRecyclerView.ViewHolder(binding.root), CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main
        fun bind(item: ApiRepo, position: Int) {
            Glide.with(this.itemView).load(item.owner.avatarUrl).circleCrop().into(binding.imageOwner)
            binding.textRepoName.text = item.name
            binding.view.setOnClickListener {
                helper.onRepoClicked(item, position)
            }
            binding.checkboxStar.setOnCheckedChangeListener(null)
            binding.checkboxStar.isChecked = item.isFavorite
            binding.checkboxStar.setOnCheckedChangeListener { _, isFavorite ->
                helper.onChangeRepoFavorite(item, isFavorite, position)
                item.isFavorite = isFavorite
            }
        }
    }

    interface RepoRecyclerCallback {
        fun onChangeRepoFavorite(repo: ApiRepo, isFavorite: Boolean, position: Int)
        fun onRepoClicked(repo: ApiRepo, position: Int)
        fun onRetryClicked()
    }
}