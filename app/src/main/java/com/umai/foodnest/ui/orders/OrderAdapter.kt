package com.umai.foodnest.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.umai.foodnest.data.model.Order
import com.umai.foodnest.data.model.OrderStatus
import com.umai.foodnest.databinding.ItemOrderBinding

class OrderAdapter(
    private val onTrack: (Order) -> Unit,
    private val onReorder: (Order) -> Unit
) : ListAdapter<Order, OrderAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemOrderBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.tvRestaurantName.text = order.restaurantName
            binding.tvItems.text = order.items.joinToString(", ") {
                "${it.foodItem.name} x${it.quantity}"
            }
            binding.tvDate.text = "Order #${order.id} • ${order.formattedDate}"
            binding.tvTotal.text = order.formattedTotal
            binding.tvStatus.text = order.status.name

            val (bgColor, textColor) = when (order.status) {
                OrderStatus.DELIVERED ->
                    Pair(com.umai.foodnest.R.color.green,
                        com.umai.foodnest.R.color.white)
                OrderStatus.ON_THE_WAY ->
                    Pair(com.umai.foodnest.R.color.orange_primary,
                        com.umai.foodnest.R.color.white)
                OrderStatus.CANCELLED ->
                    Pair(com.umai.foodnest.R.color.red,
                        com.umai.foodnest.R.color.white)
                else ->
                    Pair(com.umai.foodnest.R.color.yellow,
                        com.umai.foodnest.R.color.black)
            }
            binding.tvStatus.setBackgroundResource(bgColor)
            binding.tvStatus.setTextColor(
                binding.root.context.getColor(textColor)
            )

            binding.btnReorder.setOnClickListener { onReorder(order) }
            binding.btnReorder.text =
                if (order.status == OrderStatus.DELIVERED ||
                    order.status == OrderStatus.CANCELLED)
                    "Reorder" else "Track Order"
            binding.btnReorder.setOnClickListener {
                if (order.status == OrderStatus.DELIVERED ||
                    order.status == OrderStatus.CANCELLED)
                    onReorder(order)
                else onTrack(order)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(old: Order, new: Order) = old.id == new.id
        override fun areContentsTheSame(old: Order, new: Order) = old == new
    }
}