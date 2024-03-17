package com.example.graduationproject.ui.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityMainBinding
import com.example.graduationproject.databinding.FragmentHomeBinding
import com.example.graduationproject.ui.listActivityCustomer.ListComponents.CustomerListActivity
import com.example.graduationproject.ui.listActivitySeller.ListComponents.SellerListActivity
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.HomeFragment
import com.example.graduationproject.ui.mainActivity.fragment.NotificationsFragment
import com.example.graduationproject.ui.mainActivity.fragment.chatFragment.ChatFragment
import com.example.graduationproject.ui.mainActivity.fragment.createPost.CreatPostActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var viewBinding: ActivityMainBinding
    private lateinit var tokenManager: TokenManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        tokenManager = TokenManager(this)

        setContentView(viewBinding.root)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val homeFragment = HomeFragment()
        val notificationsFragment = NotificationsFragment()
        val chatFragment = ChatFragment()
//        val listFragment = ListFragment()

        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> setCurrentFragment(homeFragment)
                R.id.navigation_notification -> setCurrentFragment(notificationsFragment)
                R.id.navigation_add_post -> startCreatePostActivity()
                R.id.navigation_chat -> setCurrentFragment(chatFragment)
                R.id.navigation_list -> navigateToList()
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        // Determine which fragment should be displayed based on your logic
        val selectedItemId = when (val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)) {
            is HomeFragment -> R.id.navigation_home
            is NotificationsFragment -> R.id.navigation_notification
            is ChatFragment -> R.id.navigation_chat
            else -> R.id.navigation_home // Set a default fragment
        }
        // Set the selected item in the bottom navigation view
        bottomNavigationView.selectedItemId = selectedItemId
    }



    private fun navigateToList() {
        val userType = tokenManager.getUserType()
        Log.d("HomeFragment", "user type: ${userType}")

        if (tokenManager.getToken().isNullOrBlank() || userType.isNullOrBlank()) {
            Toast.makeText(this, "Login!", Toast.LENGTH_SHORT).show()
        } else {
            // Token and user type exist
            if (userType == "Seller") {
                // Navigate to SellerListActivity
                val intent = Intent(this, SellerListActivity::class.java)
                startActivity(intent)

            } else if (userType == "Customer") {
                // Navigate to BuyerActivity
                val intent = Intent(this, CustomerListActivity::class.java)
                startActivity(intent)
            }
        }
    }


    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

    private fun startCreatePostActivity() {
        val intent = Intent(this, CreatPostActivity::class.java)
        startActivity(intent)

    }
}

//    private fun onClickAddPost() {
//        viewBinding.addButton.setOnClickListener {
//            val intent = Intent(this, CreatPostActivity::class.java)
//            startActivity(intent)
//        }
//    }