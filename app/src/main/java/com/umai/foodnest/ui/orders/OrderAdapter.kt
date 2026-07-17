package com.umai.foodnest.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umai.foodnest.R
import com.umai.foodnest.data.model.Order
import com.umai.foodnest.data.model.OrderStatus
import com.umai.foodnest.databinding.ItemOrderBinding

class OrderAdapter(
    private val onTrack: (Order) -> Unit,
    private val onReorder: (Order) -> Unit,
    private val onReview: (Order) -> Unit
) : ListAdapter<Order, OrderAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemOrderBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.tvRestaurantName.text = order.restaurantName
            binding.tvItems.text = order.items.joinToString(" • ") {
                "${it.foodItem.name} x${it.quantity}"
            }
            binding.tvDate.text = "Order #${order.id} • ${order.formattedDate}"
            binding.tvTotal.text = order.formattedTotal

            val (statusText, statusColor) = when (order.status) {
                OrderStatus.DELIVERED -> "✅ Delivered" to R.color.green
                OrderStatus.ON_THE_WAY -> "🛵 On the way" to R.color.orange_primary
                OrderStatus.PREPARING -> "👨‍🍳 Preparing" to R.color.yellow
                OrderStatus.CONFIRMED -> "✔ Confirmed" to R.color.blue
                OrderStatus.CANCELLED -> "❌ Cancelled" to R.color.red
            }
            binding.tvStatus.text = statusText
            binding.tvStatus.setTextColor(binding.root.context.getColor(statusColor))

            Glide.with(binding.root)
                .load(order.restaurantImage)
                .centerCrop()
                .placeholder(R.color.gray_light)
                .into(binding.ivRestaurantThumb)

            binding.btnReorder.text = when (order.status) {
                OrderStatus.DELIVERED, OrderStatus.CANCELLED -> "Reorder"
                else -> "Track Order"
            }
            binding.btnReorder.setOnClickListener {
                when (order.status) {
                    OrderStatus.DELIVERED -> onReview(order)
                    OrderStatus.CANCELLED -> onReorder(order)
                    else -> onTrack(order)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(a: Order, b: Order) = a.id == b.id
        override fun areContentsTheSame(a: Order, b: Order) = a == b
    }
}