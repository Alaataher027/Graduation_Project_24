package com.example.graduationproject.ui.mainActivity.fragment.notification.sellerNotification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.ItemNotificationWaitingBinding
import android.text.format.DateUtils
import android.util.Log
import androidx.viewbinding.ViewBinding
import com.example.graduationproject.api.model.notifications.accANDrej.DataItem
import com.example.graduationproject.api.model.notifications.yesAndNo.DataItemS
import com.example.graduationproject.databinding.ItemNotificationConfirmBinding
import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel
import com.example.graduationproject.ui.mainActivity.fragment.notification.NotificationActionCallback
import java.text.SimpleDateFormat
import java.util.*

class NotificationSellerAdapter(
    private val viewModel: NotificationsSellerViewModel,
    private val userDataHomeViewModel: UserDataHomeViewModel,
    private val accessToken: String,
    private val callback: NotificationActionCallback
) : RecyclerView.Adapter<NotificationSellerAdapter.NotificationViewHolder>() {

    var notifications: List<Any?> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    companion object {
        private const val VIEW_TYPE_WAITING = 1
        private const val VIEW_TYPE_CONFIRM = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (notifications[position]) {
            is DataItem -> VIEW_TYPE_WAITING
            is DataItemS -> VIEW_TYPE_CONFIRM
            else -> throw IllegalArgumentException("Unknown type at position $position: ${notifications[position]?.javaClass?.name}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = when (viewType) {
            VIEW_TYPE_CONFIRM -> ItemNotificationConfirmBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            VIEW_TYPE_WAITING -> ItemNotificationWaitingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            else -> throw IllegalArgumentException("Unknown view type")
        }
        return NotificationViewHolder(binding, viewType)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        when (holder.viewType) {
            VIEW_TYPE_WAITING -> holder.bindWaiting(notification as DataItem)
            VIEW_TYPE_CONFIRM -> holder.bindConfirm(notification as DataItemS)
        }
    }

    override fun getItemCount(): Int = notifications.size

    private val userNameCache = mutableMapOf<Int, String>()

    inner class NotificationViewHolder(
        private val binding: ViewBinding,
        val viewType: Int
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindWaiting(dataItem: DataItem) {
            val waitingBinding = binding as ItemNotificationWaitingBinding
            waitingBinding.timeNotification.text = getTimeAgo(dataItem.createdAt)

            dataItem.fromWho?.let { userId ->
                val cachedName = userNameCache[userId.toInt()]
                if (cachedName != null) {
                    waitingBinding.name.text = cachedName
                } else {
                    waitingBinding.name.text = "Unknown"
                    userDataHomeViewModel.getData(accessToken, userId.toInt(),
                        onDataLoaded = { data ->
                            val name = data?.name ?: "Unknown"
                            waitingBinding.name.text = name
                            userNameCache[userId.toInt()] = name
                        },
                        onError = { errorMessage ->
                            Log.e("NotificationAdapter", "Error fetching user data: $errorMessage")
                            waitingBinding.name.text = "Unknown"
                        }
                    )
                }
            }

            waitingBinding.acceptBtn.setOnClickListener {
                dataItem.linkedId?.let {
                    viewModel.acceptOrRejectOrder(it, "Accept", callback)
                }
            }

            waitingBinding.cancelBtn.setOnClickListener {
                dataItem.linkedId?.let {
                    viewModel.acceptOrRejectOrder(it, "Reject", callback)
                }
            }
        }

        fun bindConfirm(dataItemS: DataItemS) {
            val confirmBinding = binding as ItemNotificationConfirmBinding
            confirmBinding.timeNotification.text = getTimeAgo(dataItemS.createdAt)

            dataItemS.buyerId?.let { userId ->
                val cachedName = userNameCache[userId.toInt()]
                if (cachedName != null) {
                    // confirmBinding.name.text = cachedName
                } else {
                    // confirmBinding.name.text = "Unknown"
                    userDataHomeViewModel.getData(accessToken, userId.toInt(),
                        onDataLoaded = { data ->
                            Log.e("NotificationAdapter", " fetched user data $data")

                        },
                        onError = { errorMessage ->
                            Log.e("NotificationAdapter", "Error fetching user data: $errorMessage")
                            // confirmBinding.name.text = "Unknown"
                        }
                    )
                }
            }

            confirmBinding.yesBtn.setOnClickListener {
                dataItemS.orderId?.let {
                    viewModel.yesOrNoOrder(it, "Yes", callback)
                }
            }

            confirmBinding.noBtn.setOnClickListener {
                dataItemS.orderId?.let {
                    viewModel.yesOrNoOrder(it, "No", callback)
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