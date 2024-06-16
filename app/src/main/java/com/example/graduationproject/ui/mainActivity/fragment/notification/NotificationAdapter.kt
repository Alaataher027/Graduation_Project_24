package com.example.graduationproject.ui.mainActivity.fragment.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.ItemNotificationWaitingBinding
import android.text.format.DateUtils
import android.util.Log
import com.example.graduationproject.api.model.notifications.accANDrej.DataItem
import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private val viewModel: NotificationsViewModel,
    private val userDataHomeViewModel: UserDataHomeViewModel,  // Add the UserDataHomeViewModel
    private val accessToken: String,  // Pass the access token
    private val callback: NotificationActionCallback
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    var notifications: List<DataItem?> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationWaitingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int = notifications.size

    inner class NotificationViewHolder(private val binding: ItemNotificationWaitingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: DataItem?) {
            binding.timeNotification.text = getTimeAgo(notification?.createdAt)

            // Fetch user data and set the name
            notification?.fromWho?.let { userId ->
                userDataHomeViewModel.getData(accessToken, userId.toInt(),
                    onDataLoaded = { data ->
                        binding.name.text = data?.name ?: "Unknown"
                    },
                    onError = { errorMessage ->
                        Log.e("NotificationAdapter", "Error fetching user data: $errorMessage")
                        binding.name.text = "Unknown"
                    }
                )
            }

            binding.acceptBtn.setOnClickListener {
                notification?.linkedId?.let {
                    viewModel.acceptOrRejectOrder(it, "Accept", callback)
                }
            }

            binding.cancelBtn.setOnClickListener {
                notification?.linkedId?.let {
                    viewModel.acceptOrRejectOrder(it, "Reject", callback)
                }
            }
        }

        private fun getTimeAgo(timestamp: String?): CharSequence {
            if (timestamp.isNullOrEmpty()) return ""

            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            val time = sdf.parse(timestamp)?.time ?: return ""

            return DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )
        }
    }
}
