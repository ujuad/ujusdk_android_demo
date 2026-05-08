package com.ujuad.demo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ujuad.demo.databinding.ItemAdnAdapterBinding
import com.ujuad.demo.model.AdnAdapterVersionModel

/**
 * @CreateDate: 2026/3/11 16:24
 * @Author: 青柠
 * @Description: 
 */
class AdnAdapterVersionDiffCallback : DiffUtil.ItemCallback<AdnAdapterVersionModel>() {
    override fun areItemsTheSame(oldItem: AdnAdapterVersionModel, newItem: AdnAdapterVersionModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AdnAdapterVersionModel, newItem: AdnAdapterVersionModel): Boolean {
        return oldItem == newItem
    }
}

class AdnAdapterVersionAdapter :
    ListAdapter<AdnAdapterVersionModel, AdnAdapterVersionAdapter.AdnAdapterVersionViewHolder>(AdnAdapterVersionDiffCallback()) {

    private var onItemClickListener: ((AdnAdapterVersionModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (AdnAdapterVersionModel) -> Unit) {
        this.onItemClickListener = listener
    }

    class AdnAdapterVersionViewHolder(
        private val binding: ItemAdnAdapterBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AdnAdapterVersionModel) {
            binding.tvAdnName.text = item.adnName
            binding.tvAdapterVersion.text = item.adapterVersion
            binding.tvAdnVersion.text = item.adnVersion
            binding.ivAdnLogo.setImageResource(item.adapterLogoResource)

        }
    }

    fun add(item: AdnAdapterVersionModel) {
        val currentList = currentList.toMutableList()
        currentList.add(item)
        submitList(currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdnAdapterVersionViewHolder {
        val binding =
            ItemAdnAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdnAdapterVersionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdnAdapterVersionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}