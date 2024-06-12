package com.example.graduationproject.ui.mainActivity.fragment.notification

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentNotificationsBinding
import com.example.graduationproject.ui.login.TokenManager

class NotificationsFragment : Fragment(R.layout.fragment_notifications), NotificationActionCallback {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var tokenManager: TokenManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationsBinding.bind(view)

        tokenManager = TokenManager(requireContext())  // Initialize TokenManager
        val factory = NotificationsViewModelFactory(tokenManager)
        notificationsViewModel = ViewModelProvider(this, factory).get(NotificationsViewModel::class.java)
        notificationAdapter = NotificationAdapter(notificationsViewModel, this)

        binding.reView.layoutManager = LinearLayoutManager(requireContext())
        binding.reView.adapter = notificationAdapter

        val typeUser = tokenManager.getUserType()
//        if (typeUser == "Seller") {
        fetchNotifications()
    }

    private fun fetchNotifications() {
        binding.notificationImage.visibility = View.INVISIBLE
        val accessToken = tokenManager.getToken()
        notificationsViewModel.fetchNotifications(accessToken!!)

        notificationsViewModel.notifications.observe(viewLifecycleOwner, { notifications ->
            notificationAdapter.notifications = notifications
        })

        notificationsViewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActionSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onActionFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
