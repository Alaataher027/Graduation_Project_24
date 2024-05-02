package com.example.graduationproject.ui.postProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.databinding.ActivityPostsProfileBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.HomePostViewModel
import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel

class PostsProfileActivity : AppCompatActivity() {

    private lateinit var viewModel: HomePostViewModel
    private lateinit var adapter: PostsProfileAdapter
    private lateinit var tokenManager: TokenManager
    private lateinit var viewBinding: ActivityPostsProfileBinding
    private lateinit var viewModelUserData: UserDataHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPostsProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        tokenManager = TokenManager(this)

        // Initialize ViewModel using ViewModelProvider.Factory
        val viewModelFactory = ViewModelFactory(tokenManager)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomePostViewModel::class.java)
        viewModelUserData = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)

        val accessToken = tokenManager.getToken() ?: ""
        adapter = PostsProfileAdapter(tokenManager, viewModel)

        viewBinding.RVPost.apply {
            layoutManager = LinearLayoutManager(this@PostsProfileActivity)
            adapter = this@PostsProfileActivity.adapter
        }

        viewModel.fetchHomePosts(accessToken)

        viewModel.homePosts.observe(this, Observer { posts ->


            adapter.clearData()
            val userId = tokenManager.getUserId()
            val userPosts = posts.filter { it?.userId == userId }

            adapter.posts = userPosts

            userPosts.forEach { post ->
                post?.userId?.let { userId ->
                    viewModelUserData.getData(accessToken, userId, { userData ->
                        userData?.let {
                            adapter.addUserData(userId, userData)
                            adapter.notifyDataSetChanged()
                        }
                    }, { error ->
                        // Handle error
                        Log.e("UserDataHomeViewModel", "Failed to get user data: $error")
                    })
                }
            }
        })

        onClickBackBtn()


    }

    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }
}