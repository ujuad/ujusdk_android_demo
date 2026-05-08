package com.ujuad.demo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ujuad.demo.databinding.ItemAdGroupDetailBinding
import com.ujusdk.adcore.model.data.AdUnitSettingModel
import com.ujusdk.adcore.public.enums.AdPlatformType

/**
 * @CreateDate: 2025/12/16 17:28
 * @Author: 青柠
 * @Description:
 */


// DiffCallback用于比较数据差异
class AdDetailDiffCallback : DiffUtil.ItemCallback<AdUnitSettingModel>() {
    override fun areItemsTheSame(oldItem: AdUnitSettingModel, newItem: AdUnitSettingModel): Boolean {
        return oldItem.soltId == newItem.soltId
    }

    override fun areContentsTheSame(oldItem: AdUnitSettingModel, newItem: AdUnitSettingModel): Boolean {
        return oldItem == newItem
    }
}

class AdGroupDetailAdapter :
    ListAdapter<AdUnitSettingModel, AdGroupDetailAdapter.AdDetailViewHolder>(AdDetailDiffCallback()) {

    private var onItemClickListener: ((AdUnitSettingModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (AdUnitSettingModel) -> Unit) {
        this.onItemClickListener = listener
    }

    class AdDetailViewHolder(
        private val binding: ItemAdGroupDetailBinding,
        private val clickListener: ((AdUnitSettingModel) -> Unit)?
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AdUnitSettingModel) {
            binding.tvAndName.text = AdPlatformType.fromValue(item.platformId).displayName
            binding.tvDetailId.text = item.soltId
            binding.tvDetailType.text = item.bidType.displayName
            binding.tvRenderType.text = if (item.isExpress) "模板渲染" else "自渲染"

            binding.root.setOnClickListener {
                clickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdDetailViewHolder {
        val binding = ItemAdGroupDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdDetailViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: AdDetailViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
