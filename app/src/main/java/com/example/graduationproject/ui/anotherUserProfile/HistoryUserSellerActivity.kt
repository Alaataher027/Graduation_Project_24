package com.example.graduationproject.ui.anotherUserProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityHistorySellerBinding
import com.example.graduationproject.databinding.ActivityHistoryUserSellerBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.postProfile.historySeller.HistorySellerAdapter
import com.example.graduationproject.ui.postProfile.historySeller.HistorySellerViewModel

class HistoryUserSellerActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityHistoryUserSellerBinding
    private lateinit var tokenManager: TokenManager
    private val viewModel: HistorySellerViewModel by viewModels()
    private var userID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHistoryUserSellerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        tokenManager = TokenManager(this)

        // Get the userID passed from AnotherSellerProfileActivity
        userID = intent.getIntExtra("USER_ID", 0)

        setupRecyclerView()
        observeViewModel()
        fetchHistory()

        onClickBackBtn()
    }

    private fun setupRecyclerView() {
        val adapter = HistorySellerAdapter(emptyList(), emptyList())
        viewBinding.RVPost.layoutManager = LinearLayoutManager(this)
        viewBinding.RVPost.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.postLiveData.observe(this) { posts ->
            Log.d("HistorySellerActivity", "Posts updated: $posts")
            val adapter = viewBinding.RVPost.adapter as HistorySellerAdapter
            adapter.updatePosts(posts)
        }

        viewModel.profileLiveData.observe(this) { profiles ->
            Log.d("HistorySellerActivity", "Profiles updated: $profiles")
            val adapter = viewBinding.RVPost.adapter as HistorySellerAdapter
            adapter.updateProfiles(profiles)
        }

        viewModel.errorLiveData.observe(this) { error ->
            Log.e("HistorySellerActivity", "Error: $error")
            // Handle error, show a message to the user, etc.
        }
    }

    private fun fetchHistory() {
        val accessToken = tokenManager.getToken()
        Log.d(
            "HistorySellerActivity",
            "Fetching history with accessToken: $accessToken, userID: $userID"
        )
        viewModel.getSellerHistory(accessToken!!, userID)
    }

    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }
}
