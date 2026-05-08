package com.ujuad.demo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ujuad.demo.databinding.ItemAdGroupListBinding

/**
 * @CreateDate: 2025/12/16 17:28
 * @Author: 青柠
 * @Description:
 */

// 数据类
data class AdSlotItem(val placementId: String)

// DiffCallback用于比较数据差异
class AdListDiffCallback : DiffUtil.ItemCallback<AdSlotItem>() {
    override fun areItemsTheSame(oldItem: AdSlotItem, newItem: AdSlotItem): Boolean {
        return oldItem.placementId == newItem.placementId
    }

    override fun areContentsTheSame(oldItem: AdSlotItem, newItem: AdSlotItem): Boolean {
        return oldItem == newItem
    }
}

class AdGroupAdapter :
    ListAdapter<AdSlotItem, AdGroupAdapter.AdSlotViewHolder>(AdListDiffCallback()) {

    private var onItemClickListener: ((AdSlotItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (AdSlotItem) -> Unit) {
        this.onItemClickListener = listener
    }

    class AdSlotViewHolder(
        private val binding: ItemAdGroupListBinding,
        private val clickListener: ((AdSlotItem) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AdSlotItem) {
            binding.tvSlotId.text = item.placementId
            binding.root.setOnClickListener {
                clickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdSlotViewHolder {
        val binding = ItemAdGroupListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdSlotViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: AdSlotViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
