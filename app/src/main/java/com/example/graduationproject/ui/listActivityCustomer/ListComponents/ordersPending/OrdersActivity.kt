package com.example.graduationproject.ui.listActivityCustomer.ListComponents.ordersPending

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.databinding.ActivityOrdersBinding
import com.example.graduationproject.ui.login.TokenManager

class OrdersActivity : AppCompatActivity() {

    private val ordersViewModel: OrdersViewModel by viewModels()
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var tokenManager: TokenManager
    private lateinit var viewBinding: ActivityOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityOrdersBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        tokenManager = TokenManager(this)
        val accessToken = tokenManager.getToken() ?: ""
        val buyerId = tokenManager.getUserId()

        ordersAdapter = OrdersAdapter(emptyList(), emptyList()) // Initialize with empty lists
        viewBinding.ordersRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@OrdersActivity)
            adapter = ordersAdapter
        }

        ordersViewModel.profilesLiveData.observe(this, { profiles ->
            ordersAdapter.updateProfiles(profiles)
        })

        ordersViewModel.postLiveData.observe(this, { posts ->
            ordersAdapter.updatePosts(posts)
        })

        ordersViewModel.errorLiveData.observe(this, { error ->
            // Handle error (e.g., show a Toast)
        })

        ordersViewModel.getOrders(accessToken, buyerId)
    }
}
