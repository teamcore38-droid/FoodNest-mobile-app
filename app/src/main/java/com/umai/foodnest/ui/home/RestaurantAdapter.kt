package com.umai.foodnest.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.umai.foodnest.R
import com.umai.foodnest.data.model.Restaurant
import com.umai.foodnest.databinding.ItemRestaurantBinding

class RestaurantAdapter(
    private val onClick: (Restaurant) -> Unit
) : ListAdapter<Restaurant, RestaurantAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemRestaurantBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(r: Restaurant) {
            binding.tvName.text = r.name
            binding.tvRatingBadge.text = r.rating.toString()
            binding.tvCategory.text = r.category
            binding.tvDeliveryTime.text = "${r.deliveryTime} min"
            binding.tvDescription.text = r.description
            binding.tvReviews.text = "${r.totalReviews} reviews"
            binding.tvOpen.text = if (r.isOpen) "● Open" else "● Closed"
            binding.tvOpen.backgroundTintList = android.content.res.ColorStateList.valueOf(
                binding.root.context.getColor(
                    if (r.isOpen) R.color.green else R.color.red))

            binding.tvDeliveryFee.text =
                if (r.deliveryFee == 0.0) "Free Delivery" else "LKR ${r.deliveryFee.toInt()} delivery"
            binding.tvDeliveryFee.backgroundTintList = android.content.res.ColorStateList.valueOf(
                binding.root.context.getColor(
                    if (r.deliveryFee == 0.0) R.color.green else R.color.orange_primary))

            Glide.with(binding.root)
                .load(r.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .centerCrop()
                .placeholder(R.color.gray_light)
                .error(R.color.orange_light)
                .into(binding.ivRestaurant)

            binding.root.setOnClickListener { onClick(r) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(a: Restaurant, b: Restaurant) = a.id == b.id
        override fun areContentsTheSame(a: Restaurant, b: Restaurant) = a == b
    }
}