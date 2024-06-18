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
import com.example.graduationproject.ui.mainActivity.fragment.notification.customerNotification.NotificationCustomerAdapter
import com.example.graduationproject.ui.mainActivity.fragment.notification.customerNotification.NotificationsCustomerViewModelFactory
import com.example.graduationproject.ui.mainActivity.fragment.notification.customerNotification.NotificationsCustomerViewModel
import com.example.graduationproject.ui.mainActivity.fragment.notification.sellerNotification.NotificationSellerAdapter
import com.example.graduationproject.ui.mainActivity.fragment.notification.sellerNotification.NotificationsSellerViewModel
import com.example.graduationproject.ui.mainActivity.fragment.notification.sellerNotification.NotificationsSellerViewModelFactory

class NotificationsFragment : Fragment(R.layout.fragment_notifications),
    NotificationActionCallback {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var notificationsCustomerViewModel: NotificationsCustomerViewModel
    private lateinit var notificationsSellerViewModel: NotificationsSellerViewModel
    private lateinit var notificationSellerAdapter: NotificationSellerAdapter
    private lateinit var notificationCustomerAdapter: NotificationCustomerAdapter
    private lateinit var userDataHomeViewModel: UserDataHomeViewModel
    private lateinit var tokenManager: TokenManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationsBinding.bind(view)

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)

        tokenManager = TokenManager(requireContext())  // Initialize TokenManager

        if (tokenManager.getUserType() == "Seller") {
            binding.reViewSeller.visibility = View.VISIBLE
            binding.reViewCustomer.visibility = View.INVISIBLE

        } else if (tokenManager.getUserType() == "Customer") {
            binding.reViewSeller.visibility = View.INVISIBLE
            binding.reViewCustomer.visibility = View.VISIBLE
        }

        val factorySeller = NotificationsSellerViewModelFactory(tokenManager)
        val factoryCustomer = NotificationsCustomerViewModelFactory(tokenManager)

        notificationsSellerViewModel =
            ViewModelProvider(this, factorySeller).get(NotificationsSellerViewModel::class.java)
        notificationsCustomerViewModel =
            ViewModelProvider(this, factoryCustomer).get(NotificationsCustomerViewModel::class.java)


        userDataHomeViewModel = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)

        val accessToken = tokenManager.getToken() ?: return  // Get access token, or return if null

        notificationSellerAdapter =
            NotificationSellerAdapter(
                notificationsSellerViewModel,
                userDataHomeViewModel,
                accessToken,
                this
            )

        notificationCustomerAdapter =
            NotificationCustomerAdapter(
                notificationsCustomerViewModel,
                userDataHomeViewModel,
                accessToken,
                this
            )

        //seller
        binding.reViewSeller.layoutManager = LinearLayoutManager(requireContext())
        binding.reViewSeller.adapter = notificationSellerAdapter

        // customer
        binding.reViewCustomer.layoutManager = LinearLayoutManager(requireContext())
        binding.reViewCustomer.adapter = notificationCustomerAdapter

        fetchSellerNotifications()
        fetchCustomerNotifications()



        if (tokenManager.getUserType() == "Seller") {
            swipeRefreshLayout.setOnRefreshListener {
                fetchSellerNotifications()
            }

        } else if (tokenManager.getUserType() == "Customer") {
            swipeRefreshLayout.setOnRefreshListener {
                fetchCustomerNotifications()
            }
        }

    }

    private fun fetchSellerNotifications() {
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

    private fun fetchCustomerNotifications() {
        binding.notificationImage.visibility = View.INVISIBLE
        val accessToken = tokenManager.getToken() ?: return

        notificationsCustomerViewModel.fetchNotifications(accessToken)

        notificationsCustomerViewModel.notifications.observe(viewLifecycleOwner, { notifications ->
            notificationCustomerAdapter.notifications = notifications
        })

        notificationsCustomerViewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
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
