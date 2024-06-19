package com.example.graduationproject.ui.mainActivity.fragment.notification.customerNotification

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.graduationproject.api.model.notifications.accANDrej.DataItem
import com.example.graduationproject.api.model.notifications.normal.DataItemm
import com.example.graduationproject.databinding.*
import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel
import java.text.SimpleDateFormat
import java.util.*
import android.text.format.DateUtils
import com.example.graduationproject.R
import com.example.graduationproject.api.model.notifications.yesAndNo.DataItemC
import com.example.graduationproject.ui.mainActivity.fragment.notification.NotificationActionCallback

class NotificationCustomerAdapter(
    private val viewModel: NotificationsCustomerViewModel,
    private val userDataHomeViewModel: UserDataHomeViewModel,
    private val accessToken: String,
    private val callback: NotificationActionCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val userNameCache = mutableMapOf<Int, String>()
    var notifications: List<Any?> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    companion object {
        private const val VIEW_TYPE_WAITING = 1
        private const val VIEW_TYPE_CONFIRM = 2
        private const val VIEW_TYPE_ACCEPTED = 3
        private const val VIEW_TYPE_REJECTED = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (notifications[position]) {
            is DataItem -> VIEW_TYPE_WAITING
            is DataItemC -> VIEW_TYPE_CONFIRM
            is DataItemm -> {
                if ((notifications[position] as DataItemm).status == "accepted") {
                    VIEW_TYPE_ACCEPTED
                } else if ((notifications[position] as DataItemm).status == "rejected") {
                    VIEW_TYPE_REJECTED
                } else {
                    throw IllegalArgumentException("Unknown status")
                }
            }
            else -> throw IllegalArgumentException("Unknown type at position $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_WAITING -> {
                val binding = ItemNotificationWaitingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                WaitingViewHolder(binding)
            }
            VIEW_TYPE_CONFIRM -> {
                val binding = ItemNotificationConfirmBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ConfirmViewHolder(binding)
            }
            VIEW_TYPE_ACCEPTED -> {
                val binding = ItemNotificationAcceptedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AcceptedViewHolder(binding)
            }
            VIEW_TYPE_REJECTED -> {
                val binding = ItemNotificationRejectedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                RejectedViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            VIEW_TYPE_WAITING -> {
                (holder as? WaitingViewHolder)?.bindViewHolder(notifications[position] as DataItem, position)
            }
            VIEW_TYPE_CONFIRM -> {
                (holder as? ConfirmViewHolder)?.bindViewHolder(notifications[position] as DataItemC, position)
            }
            VIEW_TYPE_ACCEPTED -> {
                (holder as? AcceptedViewHolder)?.bindViewHolder(notifications[position] as DataItemm, position)
            }
            VIEW_TYPE_REJECTED -> {
                (holder as? RejectedViewHolder)?.bindViewHolder(notifications[position] as DataItemm, position)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun getItemCount(): Int = notifications.size



    inner abstract class NotificationViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        abstract fun bindViewHolder(notification: Any?, position: Int = -1)

        fun getTimeAgo(timestamp: String?): CharSequence {
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


    // notification for two buttons ( Accept Or Reject )

    inner class WaitingViewHolder(private val binding: ItemNotificationWaitingBinding) : NotificationViewHolder(binding) {

        override fun bindViewHolder(notification: Any?, position: Int) {
            val dataItem = notification as? DataItem ?: return

            binding.timeNotification.text = getTimeAgo(dataItem.createdAt)

            dataItem.fromWho?.let { userId ->
                val cachedName = userNameCache[userId.toInt()]
                if (cachedName != null) {
                    binding.name.text = cachedName
                } else {
                    binding.name.text = "Unknown"
                    userDataHomeViewModel.getData(
                        accessToken,
                        userId.toInt(),
                        onDataLoaded = { data ->
                            val name = data?.name ?: "Unknown"
                            binding.name.text = name
                            userNameCache[userId.toInt()] = name
                        },
                        onError = { errorMessage ->
                            binding.name.text = "Unknown"
                            Log.e("NotificationAdapterC", "Error fetching user data: $errorMessage")
                        }
                    )
                }
            }

            binding.acceptBtn.setOnClickListener {
                dataItem.linkedId?.let {
                    viewModel.acceptOrRejectOrder(it, "Accept", callback)
                }
            }

            binding.cancelBtn.setOnClickListener {
                dataItem.linkedId?.let {
                    viewModel.acceptOrRejectOrder(it, "Reject", callback)
                }
            }
        }
    }

    // notification for two buttons ( Yes Or No )

    inner class ConfirmViewHolder(private val binding: ItemNotificationConfirmBinding) : NotificationViewHolder(binding) {

        override fun bindViewHolder(notification: Any?, position: Int) {
            val dataItemC = notification as? DataItemC ?: return

            binding.timeNotification.text = getTimeAgo(dataItemC.createdAt)

            dataItemC.sellerId?.let { userId ->
                val cachedName = userNameCache[userId.toInt()]
                if (cachedName != null) {
                    // binding.name.text = cachedName
                } else {
                    // binding.name.text = "Unknown"
                    userDataHomeViewModel.getData(
                        accessToken,
                        userId.toInt(),
                        onDataLoaded = { data ->
                            // Log.e("NotificationCustomerAdapter", " fetched user data $data")
                        },
                        onError = { errorMessage ->
                            // Log.e("NotificationCustomerAdapter", "Error fetching user data: $errorMessage")
                            // binding.name.text = "Unknown"
                        }
                    )
                }
            }

            binding.yesBtn.setOnClickListener {
                dataItemC.orderId?.let {
                    viewModel.yesOrNoOrder(it, "Yes", callback)
                }
            }

            binding.noBtn.setOnClickListener {
                dataItemC.orderId?.let {
                    viewModel.yesOrNoOrder(it, "No", callback)
                }
            }
        }
    }

    // notification Accept

    inner class AcceptedViewHolder(private val binding: ItemNotificationAcceptedBinding) : NotificationViewHolder(binding) {

        override fun bindViewHolder(notification: Any?, position: Int) {
            val dataItemm = notification as? DataItemm ?: return

            binding.timeNotification.text = getTimeAgo(dataItemm.createdAt)

            dataItemm.toWho?.let { userId ->
                val cachedName = userNameCache[userId.toInt()]
                if (cachedName != null) {
                    binding.name.text = cachedName
                } else {
                    binding.name.text = "Unknown"
                    userDataHomeViewModel.getData(
                        accessToken,
                        userId.toInt(),
                        onDataLoaded = { data ->
                            val name = data?.name ?: "Unknown"
                            binding.name.text = name
                            userNameCache[userId.toInt()] = name
                        },
                        onError = { errorMessage ->
                            binding.name.text = "Unknown"
                            Log.e("NotificationAdapterC", "Error fetching user data: $errorMessage")
                        }
                    )
                }
            }


            binding.accept.text = "Accepted your request"
            binding.checkMark.setImageResource(R.drawable.check_green)
            binding.checkMark.visibility = View.VISIBLE
        }
    }

    // notification reject

    inner class RejectedViewHolder(private val binding: ItemNotificationRejectedBinding) : NotificationViewHolder(binding) {

        override fun bindViewHolder(notification: Any?, position: Int) {
            val dataItemm = notification as? DataItemm ?: return

            binding.timeNotification.text = getTimeAgo(dataItemm.createdAt)

            dataItemm.toWho?.let { userId ->
                val cachedName = userNameCache[userId.toInt()]
                if (cachedName != null) {
                    binding.name.text = cachedName
                } else {
                    binding.name.text = "Unknown"
                    userDataHomeViewModel.getData(
                        accessToken,
                        userId.toInt(),
                        onDataLoaded = { data ->
                            val name = data?.name ?: "Unknown"
                            binding.name.text = name
                            userNameCache[userId.toInt()] = name
                        },
                        onError = { errorMessage ->
                            binding.name.text = "Unknown"
                            Log.e("NotificationAdapterC", "Error fetching user data: $errorMessage")
                        }
                    )
                }
            }

            binding.reject.text = "Rejected your request"
            binding.rejectMark.setImageResource(R.drawable.baseline_cancel_24)
            binding.rejectMark.visibility = View.VISIBLE
        }
    }
}