package com.example.gitapp.ui.stargazers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitapp.databinding.ItemStargazerBinding
import com.example.gitapp.entity.Stargazer

class StargazersAdapter(
    private val stargazersList: List<Stargazer>
) : RecyclerView.Adapter<StargazersAdapter.StargazerViewHolder>() {
    inner class StargazerViewHolder(private val binding: ItemStargazerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(stargazer: Stargazer) {
            binding.textStargazerName.text = stargazer.user.nameUser
            binding.textStaredDate.text = stargazer.time.toString()
            Glide.with(binding.view).load(stargazer.user.avatarUrl).circleCrop().into(binding.imageStargazer)
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