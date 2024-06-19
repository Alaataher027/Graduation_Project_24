package com.example.graduationproject.ui.anotherUserProfile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.api.model.post.postHome.DataItem
import com.example.graduationproject.api.model.profile.Data
import com.example.graduationproject.databinding.ActivityUserPostsBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.HomePostViewModel
import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel

class UserPostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserPostsBinding
    private lateinit var viewModel: HomePostViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var userDataViewModel: UserDataHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize TokenManager
        tokenManager = TokenManager(this)

        // Initialize ViewModel for home posts
        val factory = HomePostViewModelFactory(tokenManager)
        viewModel = ViewModelProvider(this, factory).get(HomePostViewModel::class.java)

        // Initialize ViewModel for user data
        userDataViewModel = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)

        val userId = intent.getIntExtra("USER_ID", 0)

        // Observe home posts
        viewModel.homePosts.observe(this) { posts ->
            val userPosts = posts.filterNotNull().filter { it.userId == userId }
            // Fetch user data and setup RecyclerView
            fetchUserDataAndSetupRecyclerView(userPosts, userId)
        }

        // Fetch home posts
        val accessToken = tokenManager.getToken() ?: ""
        viewModel.fetchHomePosts(accessToken)
    }

    private fun fetchUserDataAndSetupRecyclerView(posts: List<DataItem>, userId: Int) {
        val accessToken = tokenManager.getToken() ?: ""
        userDataViewModel.getData(accessToken, userId,
            onDataLoaded = { userData ->
                userData?.let {
                    setupRecyclerView(posts, it)
                }
            },
            onError = { error ->
                // Handle error
                Log.e("UserDataFetchError", error)
            }
        )
    }

    fun onBackPressed(view: View) {
        super.onBackPressed()
        // Optionally add additional functionality here
    }

    private fun setupRecyclerView(posts: List<DataItem>, userData: Data) {
        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(this)
        // Pass TokenManager and HomePostViewModel to the adapter constructor
        binding.recyclerViewPosts.adapter = UserPostsAdapter(tokenManager, viewModel, posts, userData, this)
    }
}