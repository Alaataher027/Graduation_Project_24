package com.example.graduationproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.graduationproject.databinding.ActivitySavedBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel

class SavedPostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedBinding
    private lateinit var viewModel: SavedPostsViewModel
    private lateinit var adapter: SavedPostsAdapter
    private lateinit var viewModelUserData: UserDataHomeViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipeRefreshLayout = binding.swipeRefreshLayout // Initialize swipeRefreshLayout

        // Initialize TokenManager
        tokenManager = TokenManager(this)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, SavedPostsViewModelFactory(tokenManager)).get(
            SavedPostsViewModel::class.java
        )

        // Initialize UserDataHomeViewModel
        viewModelUserData = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)

        viewModel.fetchSavedPosts()

        // Initialize RecyclerView and Adapter
        adapter = SavedPostsAdapter { post ->
            post?.id?.let { postId ->
                val intent = Intent(this, ViewOnePostActivity::class.java)
                intent.putExtra("POST_ID", postId)
                startActivity(intent)
            }
        }
        binding.postList.layoutManager = LinearLayoutManager(this)
        binding.postList.adapter = adapter

        // Observe LiveData for saved posts
        viewModel.savedPosts.observe(this, { savedPosts ->
            savedPosts.forEach { post ->
                post?.userId?.let { userId ->
                    // Fetch user data for each post
                    fetchUserData(userId)
                }
            }
            adapter.savedPosts = savedPosts
            // Now that saved posts are fetched, fetch user data for each post
        })

        binding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }

        refreshPosts()


    }

    fun refreshPosts(){
        swipeRefreshLayout.setOnRefreshListener {
            // Observe LiveData for saved posts
            viewModel.savedPosts.observe(this, { savedPosts ->
                savedPosts.forEach { post ->
                    post?.userId?.let { userId ->
                        // Fetch user data for each post
                        fetchUserData(userId)
                    }
                }
                adapter.savedPosts = savedPosts
                // Now that saved posts are fetched, fetch user data for each post
            })

            swipeRefreshLayout.isRefreshing = false

        }

    }
    private fun fetchUserData(userId: Int) {
        val accessToken = tokenManager.getToken() ?: ""
        viewModelUserData.getData(accessToken, userId, { userData ->
            userData?.let {
                adapter.addUserData(userId, userData)
            }
        }, { error ->
            // Handle error
            Log.e("UserDataHomeViewModel", "Failed to get user data: $error")
        })
    }
}
