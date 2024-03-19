package com.example.graduationproject.ui.mainActivity.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentHomeBinding
import com.example.graduationproject.ui.login.TokenManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: HomePostViewModel
    private lateinit var adapter: PostAdapter
    private lateinit var tokenManager: TokenManager
    private lateinit var viewBinding: FragmentHomeBinding
    private lateinit var viewModelUserData: UserDataHomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentHomeBinding.bind(view)
        tokenManager = TokenManager(requireContext())

        // Initialize ViewModel
//        viewModel = ViewModelProvider(this).get(HomePostViewModel::class.java)
        viewModel = HomePostViewModel(tokenManager)
        viewModelUserData = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)

        // Initialize RecyclerView adapter
        adapter = PostAdapter()

        // Set up RecyclerView
        viewBinding.RVPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }

        // Fetch home posts
        val accessToken = tokenManager.getToken() ?: ""
        viewModel.fetchHomePosts(accessToken)

        val userId = tokenManager.getUserPostId()

        // Observe user data from ViewModel
        viewModelUserData.getData(accessToken, userId, { userData ->
            userData?.let {
                // Pass user name and image to adapter
                adapter.setName(userData)
                adapter.setImage(userData)
            }
        }, { error ->
            // Handle error
            Log.e("UserDataHomeViewModel", "Failed to get user data: $error")
        })

        // Observe LiveData from ViewModel to update UI with home posts
        viewModel.homePosts.observe(viewLifecycleOwner, Observer { posts ->
            // Reverse the list before setting it to the adapter
            adapter.posts = posts
        })

    }
}

