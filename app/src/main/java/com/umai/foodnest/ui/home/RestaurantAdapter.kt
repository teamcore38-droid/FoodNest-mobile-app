package com.umai.foodnest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umai.foodnest.data.model.Restaurant
import com.umai.foodnest.databinding.ItemRestaurantBinding

class RestaurantAdapter(
    private val onClickListener: (Restaurant) -> Unit
) : ListAdapter<Restaurant, RestaurantAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemRestaurantBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurant: Restaurant) {
            binding.tvName.text = restaurant.name
            binding.tvRating.text = "⭐ ${restaurant.rating}"
            binding.tvRatingBadge.text = "${restaurant.rating}"
            binding.tvDeliveryTime.text = "${restaurant.deliveryTime} min"
            binding.tvOpen.text = if (restaurant.isOpen) "Open" else "Closed"
            binding.tvOpen.setBackgroundResource(
                if (restaurant.isOpen) com.umai.foodnest.R.drawable.bg_open_badge
                else com.umai.foodnest.R.drawable.bg_closed_badge
            )

            Glide.with(binding.root)
                .load(restaurant.imageUrl)
                .centerCrop()
                .placeholder(com.umai.foodnest.R.color.gray_light)
                .into(binding.ivRestaurant)

            binding.root.setOnClickListener { onClickListener(restaurant) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(old: Restaurant, new: Restaurant) = old.id == new.id
        override fun areContentsTheSame(old: Restaurant, new: Restaurant) = old == new
    }
}