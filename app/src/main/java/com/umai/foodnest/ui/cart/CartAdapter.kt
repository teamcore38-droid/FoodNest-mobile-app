package com.umai.foodnest.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umai.foodnest.data.model.CartItem
import com.umai.foodnest.databinding.ItemCartBinding

class CartAdapter(
    private val onPlus: (CartItem) -> Unit,
    private val onMinus: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemCartBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            binding.tvCartName.text = item.foodItem.name
            binding.tvCartPrice.text = "LKR %.2f".format(item.totalPrice)
            binding.tvQuantity.text = item.quantity.toString()

            Glide.with(binding.root)
                .load(item.foodItem.imageUrl)
                .centerCrop()
                .placeholder(com.umai.foodnest.R.color.gray_light)
                .into(binding.ivCartItem)

            binding.btnPlus.setOnClickListener { onPlus(item) }
            binding.btnMinus.setOnClickListener { onMinus(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(old: CartItem, new: CartItem) =
            old.foodItem.id == new.foodItem.id
        override fun areContentsTheSame(old: CartItem, new: CartItem) =
            old == new
    }
}