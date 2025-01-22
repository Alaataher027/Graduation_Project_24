package com.example.graduationproject.ui.anotherUserProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityHistoryUserCustomerBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.postProfile.historyCustomer.HistoryCustomerAdapter
import com.example.graduationproject.ui.postProfile.historyCustomer.HistoryCustomerViewModel
import com.example.graduationproject.ui.postProfile.historySeller.HistorySellerAdapter
import com.example.graduationproject.ui.postProfile.historySeller.HistorySellerViewModel

class HistoryUserCustomerActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityHistoryUserCustomerBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var viewModelSeller: HistorySellerViewModel
    private lateinit var viewModelCustomer: HistoryCustomerViewModel
    private var userID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHistoryUserCustomerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        tokenManager = TokenManager(this)

        // Get the userID from the intent
        userID = intent.getIntExtra("USER_ID", -1)

        // Initialize the ViewModel
        viewModelSeller = ViewModelProvider(this).get(HistorySellerViewModel::class.java)
        viewModelCustomer = ViewModelProvider(this).get(HistoryCustomerViewModel::class.java)

        setupRecyclerView()
        observeViewModel()
        fetchHistory()

        onClickBackBtn()

        viewBinding.buyBtn.setOnClickListener {
            viewBinding.RVPostSell.visibility = View.INVISIBLE //GONE
            viewBinding.RVPostBuy.visibility = View.VISIBLE
            viewBinding.buyBtn.setBackgroundResource(R.drawable.rec_his_g)
            viewBinding.sellBtn.setBackgroundResource(R.drawable.rectangle_bord)
            viewBinding.buyBtn.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            viewBinding.sellBtn.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.gray_dark
                )
            )
        }

        viewBinding.sellBtn.setOnClickListener {
            viewBinding.RVPostSell.visibility = View.VISIBLE
            viewBinding.RVPostBuy.visibility = View.INVISIBLE //GONE
            viewBinding.buyBtn.setBackgroundResource(R.drawable.rectangle_bord)
            viewBinding.sellBtn.setBackgroundResource(R.drawable.rec_his_g)
            viewBinding.buyBtn.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.gray_dark
                )
            )
            viewBinding.sellBtn.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
        }
    }

    private fun setupRecyclerView() {
        val sellerAdapter = HistorySellerAdapter(emptyList(), emptyList())
        viewBinding.RVPostSell.layoutManager = LinearLayoutManager(this)
        viewBinding.RVPostSell.adapter = sellerAdapter

        val buyAdapter = HistoryCustomerAdapter(emptyList(), emptyList())
        viewBinding.RVPostBuy.layoutManager = LinearLayoutManager(this)
        viewBinding.RVPostBuy.adapter = buyAdapter
    }

    private fun observeViewModel() {
        viewModelSeller.postLiveData.observe(this) { posts ->
            Log.d("HistorySellerActivity", "Posts updated: $posts")
            val adapter = viewBinding.RVPostSell.adapter as HistorySellerAdapter
            adapter.updatePosts(posts)
        }

        viewModelSeller.profileLiveData.observe(this) { profiles ->
            Log.d("HistorySellerActivity", "Profiles updated: $profiles")
            val adapter = viewBinding.RVPostSell.adapter as HistorySellerAdapter
            adapter.updateProfiles(profiles)
        }

        viewModelSeller.errorLiveData.observe(this) { error ->
            Log.e("HistorySellerActivity", "Error: $error")
            // Handle error, show a message to the user, etc.
        }

        viewModelCustomer.postLiveData.observe(this) { posts ->
            Log.d("HistoryCustomerActivity", "Buy posts updated: $posts")
            val adapter = viewBinding.RVPostBuy.adapter as HistoryCustomerAdapter
            adapter.updatePosts(posts)
        }

        viewModelCustomer.profileLiveData.observe(this) { profiles ->
            Log.d("HistoryCustomerActivity", "Buy profiles updated: $profiles")
            val adapter = viewBinding.RVPostBuy.adapter as HistoryCustomerAdapter
            adapter.updateProfiles(profiles)
        }

        viewModelCustomer.errorLiveData.observe(this) { error ->
            Log.e("HistoryCustomerActivity", "Error: $error")
            // Handle error, show a message to the user, etc.
        }
    }

    private fun fetchHistory() {
        val accessToken = tokenManager.getToken()
        userID?.let {
            if (it != -1) {
                Log.d("HistoryCustomerActivity", "Fetching history with accessToken: $accessToken, userID: $it")
                viewModelSeller.getSellerHistory(accessToken!!, it)
                viewModelCustomer.getCustomerHistory(accessToken!!, it)
            } else {
                Log.e("HistoryCustomerActivity", "Error: userID is invalid")
                // Handle error, show a message to the user, etc.
            }
        } ?: run {
            Log.e("HistoryCustomerActivity", "Error: userID is null")
            // Handle error, show a message to the user, etc.
        }
    }

    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }
}
