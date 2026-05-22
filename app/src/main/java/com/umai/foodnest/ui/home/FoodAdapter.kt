package com.umai.foodnest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umai.foodnest.data.model.FoodItem
import com.umai.foodnest.databinding.ItemFoodBinding

class FoodAdapter(
    private val onAddToCart: (FoodItem) -> Unit
) : ListAdapter<FoodItem, FoodAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemFoodBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FoodItem) {
            binding.tvFoodName.text = item.name
            binding.tvFoodDesc.text = item.description
            binding.tvFoodPrice.text = "LKR %.2f".format(item.price)

            Glide.with(binding.root)
                .load(item.imageUrl)
                .centerCrop()
                .placeholder(com.umai.foodnest.R.color.gray_light)
                .into(binding.ivFood)

            binding.btnAdd.setOnClickListener {
                onAddToCart(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFoodBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<FoodItem>() {
        override fun areItemsTheSame(old: FoodItem, new: FoodItem) = old.id == new.id
        override fun areContentsTheSame(old: FoodItem, new: FoodItem) = old == new
    }
}