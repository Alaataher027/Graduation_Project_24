package com.example.graduationproject.ui.mainActivity.fragment.notification

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentNotificationsBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel
import com.example.graduationproject.ui.mainActivity.fragment.notification.sellerNotification.NotificationSellerAdapter
import com.example.graduationproject.ui.mainActivity.fragment.notification.sellerNotification.NotificationsSellerViewModel
import com.example.graduationproject.ui.mainActivity.fragment.notification.sellerNotification.NotificationsViewModelFactory

class NotificationsFragment : Fragment(R.layout.fragment_notifications),
    NotificationActionCallback {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationsSellerViewModel: NotificationsSellerViewModel
    private lateinit var notificationSellerAdapter: NotificationSellerAdapter
    private lateinit var userDataHomeViewModel: UserDataHomeViewModel
    private lateinit var tokenManager: TokenManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationsBinding.bind(view)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        tokenManager = TokenManager(requireContext())  // Initialize TokenManager
        val factory = NotificationsViewModelFactory(tokenManager)
        notificationsSellerViewModel =
            ViewModelProvider(this, factory).get(NotificationsSellerViewModel::class.java)
        userDataHomeViewModel = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)

        val accessToken = tokenManager.getToken() ?: return  // Get access token, or return if null

        notificationSellerAdapter =
            NotificationSellerAdapter(notificationsSellerViewModel, userDataHomeViewModel, accessToken, this)

        binding.reView.layoutManager = LinearLayoutManager(requireContext())
        binding.reView.adapter = notificationSellerAdapter

        fetchNotifications()
        swipeRefreshLayout.setOnRefreshListener {
            fetchNotifications()
        }
    }

    private fun fetchNotifications() {
        binding.notificationImage.visibility = View.INVISIBLE
        val accessToken = tokenManager.getToken() ?: return

        notificationsSellerViewModel.fetchNotifications(accessToken)

        notificationsSellerViewModel.notifications.observe(viewLifecycleOwner, { notifications ->
            notificationSellerAdapter.notifications = notifications
        })

        notificationsSellerViewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })

        swipeRefreshLayout.isRefreshing = false

    }

    override fun onActionSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onActionFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}