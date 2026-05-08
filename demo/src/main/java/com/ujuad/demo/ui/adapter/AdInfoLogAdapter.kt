package com.ujuad.demo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ujuad.demo.databinding.ItemAdInfoLogBinding

/**
 * @CreateDate: 2025/12/16 17:28
 * @Author: 青柠
 * @Description:
 */


// DiffCallback用于比较数据差异
class AdInfoDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

class AdInfoLogAdapter :
    ListAdapter<String, AdInfoLogAdapter.AdInfoLogViewHolder>(AdInfoDiffCallback()) {

    class AdInfoLogViewHolder(
        private val binding: ItemAdInfoLogBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvInfo.text = item
        }
    }

    fun add(item: String) {
        val currentList = currentList.toMutableList()
        currentList.add(item)
        submitList(currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdInfoLogViewHolder {
        val binding =
            ItemAdInfoLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdInfoLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdInfoLogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
