package com.umai.foodnest.ui.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umai.foodnest.data.model.Offer
import com.umai.foodnest.databinding.ItemOfferBinding

class OffersAdapter(
    private val offers: List<Offer>
) : RecyclerView.Adapter<OffersAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemOfferBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(offer: Offer) {
            binding.tvOfferTitle.text = offer.title
            binding.tvOfferDesc.text = offer.description
            binding.tvOfferCode.text = "Code: ${offer.code}"
            Glide.with(binding.root)
                .load(offer.imageUrl)
                .centerCrop()
                .into(binding.ivOffer)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemOfferBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(offers[position])

    override fun getItemCount() = offers.size
}