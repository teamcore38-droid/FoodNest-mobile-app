package com.umai.foodnest.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umai.foodnest.data.model.AppNotification
import com.umai.foodnest.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private val notifications: List<AppNotification>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemNotificationBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(notif: AppNotification) {
            binding.tvNotifTitle.text = notif.title
            binding.tvNotifMessage.text = notif.message
            binding.tvNotifTime.text = SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(Date(notif.timestamp))
            binding.tvNotifIcon.text = when (notif.type) {
                "order" -> "🛍️"
                "promo" -> "🎁"
                else -> "🔔"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(notifications[position])

    override fun getItemCount() = notifications.size
}