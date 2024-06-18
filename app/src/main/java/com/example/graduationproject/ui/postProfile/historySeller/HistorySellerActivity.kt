package com.example.graduationproject.ui.postProfile.historySeller

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.databinding.ActivityHistorySellerBinding
import com.example.graduationproject.ui.login.TokenManager

class HistorySellerActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityHistorySellerBinding
    private lateinit var tokenManager: TokenManager
    private val viewModel: HistorySellerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHistorySellerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        tokenManager = TokenManager(this)

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
//        viewModel.historiesLiveData.observe(this) { histories ->
//            Log.d("HistorySellerActivity", "Histories updated: $histories")
//            val adapter = viewBinding.RVPost.adapter as HistorySellerAdapter
//            adapter.updateHistories(histories)
//        }

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
        val userID = tokenManager.getUserId() // Assuming you have a method to get the user ID
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
