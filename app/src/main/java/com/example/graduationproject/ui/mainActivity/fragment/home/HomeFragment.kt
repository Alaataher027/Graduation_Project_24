package com.example.graduationproject.ui.mainActivity.fragment.home

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentHomeBinding
import com.example.graduationproject.ui.login.TokenManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.api.model.post.postHome.DataItem

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewModel: HomePostViewModel
    private lateinit var adapter: PostAdapter
    private lateinit var tokenManager: TokenManager
    private lateinit var viewBinding: FragmentHomeBinding
    private lateinit var viewModelUserData: UserDataHomeViewModel
    private lateinit var originalPosts: List<DataItem?> // Variable to store original posts

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentHomeBinding.bind(view)
        tokenManager = TokenManager(requireContext())

        // Initialize ViewModel
        viewModel = HomePostViewModel(tokenManager)
        viewModelUserData = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)

        val progressBar = viewBinding.progressBar
        val progressDrawable = progressBar.indeterminateDrawable.mutate()
        progressDrawable.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.my_light_primary),
            PorterDuff.Mode.SRC_IN
        )
        progressBar.indeterminateDrawable = progressDrawable

        val accessToken = tokenManager.getToken() ?: ""
        // Initialize RecyclerView adapter
        adapter = PostAdapter(tokenManager, viewModel)

        // Set up RecyclerView
        viewBinding.RVPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }

        // Fetch home posts
        viewModel.fetchHomePosts(accessToken)

        val userId = tokenManager.getUserPostId()

        // Observe LiveData from ViewModel to update UI with home posts
        viewModel.homePosts.observe(viewLifecycleOwner, Observer { posts ->
            originalPosts = posts // Save original posts
            // Reverse the list before setting it to the adapter
            val userIds = posts.mapNotNull { it?.userId }
            userIds.distinct().forEach { userId ->
                viewModelUserData.getData(accessToken, userId, { userData ->
                    userData?.let {
                        viewBinding.progressBar.visibility = View.INVISIBLE
                        adapter.addUserData(userId, userData)
                    }
                }, { error ->
                    // Handle error
                    Log.e("UserDataHomeViewModel", "Failed to get user data: $error")
                })
            }
            adapter.posts = posts
        })


        // (1) Plastic button
        viewBinding.plasticBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.plasticBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.plasticBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            filterPosts(listOf("بلاستيك", "plastic", "Plastic"))
        }

        // (2) Metal button
        viewBinding.metalBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.metalBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.metalBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            filterPosts(listOf("معدن", "metal"))
        }

        // (3) Glass button
        viewBinding.glassBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.glassBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.glassBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            filterPosts(listOf("زجاج", "glass"))
        }

        // (4) Paper button
        viewBinding.paperBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.paperBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.paperBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            filterPosts(listOf("ورق", "paper"))
        }

        // (5) Steel button
        viewBinding.steelBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.steelBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.steelBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            filterPosts(listOf("حديد", "steel"))
        }

        // (6) Wood button
        viewBinding.woodBtn.setOnClickListener {
            resetButtonsState() // Reset other buttons' states
            viewBinding.woodBtn.setBackgroundResource(R.drawable.rec_press)
            viewBinding.woodBtn.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            filterPosts(listOf("خشب", "wood"))
        }

    }

    // Function to reset buttons' states
    private fun resetButtonsState() {
        // Reset all buttons' background and text color to default
        viewBinding.plasticBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.plasticBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.metalBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.metalBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.glassBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.glassBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.paperBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.paperBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.steelBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.steelBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        viewBinding.woodBtn.setBackgroundResource(R.drawable.rectangle_bord)
        viewBinding.woodBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    // Function to filter posts
    private fun filterPosts(materials: List<String>) {
        val filteredPosts = viewModel.filterPostsByMaterial(originalPosts, materials)
        adapter.posts = filteredPosts
    }

}

//import android.graphics.PorterDuff
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.example.graduationproject.R
//import com.example.graduationproject.databinding.FragmentHomeBinding
//import com.example.graduationproject.ui.login.TokenManager
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.LinearLayoutManager
//
//class HomeFragment : Fragment(R.layout.fragment_home) {
//
//    private lateinit var viewModel: HomePostViewModel
//    private lateinit var adapter: PostAdapter
//    private lateinit var tokenManager: TokenManager
//    private lateinit var viewBinding: FragmentHomeBinding
//    private lateinit var viewModelUserData: UserDataHomeViewModel
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewBinding = FragmentHomeBinding.bind(view)
//        tokenManager = TokenManager(requireContext())
//
//        // Initialize ViewModel
//        viewModel = ViewModelProvider(this).get(HomePostViewModel::class.java)
//        viewModelUserData = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)
//
//        // Initialize RecyclerView adapter
//        adapter = PostAdapter(tokenManager)
//
//        val progressBar = viewBinding.progressBar
//        val progressDrawable = progressBar.indeterminateDrawable.mutate()
//        progressDrawable.setColorFilter(
//            ContextCompat.getColor(requireContext(), R.color.my_light_primary),
//            PorterDuff.Mode.SRC_IN
//        )
//        progressBar.indeterminateDrawable = progressDrawable
//
//        // Set up RecyclerView
//        viewBinding.RVPost.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = this@HomeFragment.adapter
//        }
//
//        // Fetch home posts
//        val accessToken = tokenManager.getToken() ?: ""
//        viewModel.fetchHomePosts(accessToken)
//
//        // Observe LiveData from ViewModel to update UI with home posts
//        viewModel.homePosts.observe(viewLifecycleOwner, Observer { posts ->
//            // Reverse the list before setting it to the adapter
//            val userIds = posts.mapNotNull { it?.userId }
//            userIds.distinct().forEach { userId ->
//                viewModelUserData.getData(accessToken, userId, { userData ->
//                    userData?.let {
//                        viewBinding.progressBar.visibility = View.INVISIBLE
//                        adapter.addUserData(userId, userData)
//                    }
//                }, { error ->
//                    // Handle error
//                    Log.e("UserDataHomeViewModel", "Failed to get user data: $error")
//                })
//            }
//            adapter.posts = posts
//        })
//    }
//}
//
