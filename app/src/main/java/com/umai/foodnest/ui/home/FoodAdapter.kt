package com.umai.foodnest.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.umai.foodnest.R
import com.umai.foodnest.data.model.FoodItem
import com.umai.foodnest.databinding.ItemFoodBinding

class FoodAdapter(
    private val onAdd: (FoodItem) -> Unit
) : ListAdapter<FoodItem, FoodAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemFoodBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FoodItem) {
            binding.tvFoodName.text = item.name
            binding.tvFoodDesc.text = item.description
            binding.tvFoodPrice.text = "LKR %.2f".format(item.price)
            binding.tvPopular.visibility = if (item.isPopular) View.VISIBLE else View.GONE

            Glide.with(binding.root)
                .load(item.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .centerCrop()
                .placeholder(R.color.gray_light)
                .into(binding.ivFood)

            binding.btnAdd.setOnClickListener { onAdd(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<FoodItem>() {
        override fun areItemsTheSame(a: FoodItem, b: FoodItem) = a.id == b.id
        override fun areContentsTheSame(a: FoodItem, b: FoodItem) = a == b
    }
}