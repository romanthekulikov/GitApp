package com.example.gitapp.ui.stargazers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitapp.data.api.models.ApiStarredData
import com.example.gitapp.databinding.ItemStargazerBinding

class StargazersAdapter(private val stargazersList: ArrayList<ApiStarredData>) :
    RecyclerView.Adapter<StargazersAdapter.StargazerViewHolder>() {
    inner class StargazerViewHolder(private val binding: ItemStargazerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stargazer: ApiStarredData) {
            binding.stargazerName.text = stargazer.user.name
            binding.staredDate.text = stargazer.time
            Glide.with(binding.view).load(stargazer.user.iconUrl).circleCrop().into(binding.stargazerIcon)
        }
    }

    override fun getItemCount(): Int {
        return stargazersList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StargazerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return StargazerViewHolder(ItemStargazerBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: StargazerViewHolder, position: Int) {
        holder.bind(stargazersList[position])
    }
}