package com.example.graduationproject.ui.mainActivity.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.FragmentHomeBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.listActivityCustomer.ListComponents.CustomerListActivity
import com.example.graduationproject.ui.listActivitySeller.ListComponents.SellerListActivity


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewBinding: FragmentHomeBinding
    private lateinit var tokenManager: TokenManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize view binding
        viewBinding = FragmentHomeBinding.bind(view)
        tokenManager = TokenManager(requireContext())


        // Set an OnClickListener for the ImageView using view binding
//        navigateToList()
        createPost()
    }

//    private fun navigateToList() {
//        viewBinding.listBtn.setOnClickListener {
//            val userType = tokenManager.getUserType()
//            Log.d("HomeFragment", "user type: ${userType}")
//
//            if (tokenManager.getToken().isNullOrBlank() || userType.isNullOrBlank()) {
//                Toast.makeText(requireContext(), "Login!", Toast.LENGTH_SHORT).show()
//            } else {
//                // Token and user type exist
//                if (userType == "Seller") {
//                    // Navigate to SellerListActivity
//                    val intent = Intent(requireContext(), SellerListActivity::class.java)
//                    startActivity(intent)
//
//                } else if (userType == "Customer") {
//                    // Navigate to BuyerActivity
//                    val intent = Intent(requireContext(), CustomerListActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//        }
//    }

    fun createPost() {
        // Assuming you have a list of posts, replace it with your actual data
        val posts = listOf(
            Post(R.drawable.person1, "John Doe", R.drawable.post_img),
            Post(R.drawable.person1, "Jane Smith", R.drawable.post_img),
            // Add more posts as needed
        )
// Create the adapter
        val adapter = PostAdapter(posts)

// Set the adapter to the RecyclerView
        viewBinding.RVPost.adapter = adapter

    }
}
