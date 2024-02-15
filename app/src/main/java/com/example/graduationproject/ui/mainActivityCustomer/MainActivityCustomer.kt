package com.example.graduationproject.ui.mainActivityCustomer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityMainCustomerBinding
import com.example.graduationproject.ui.mainActivityCustomer.fragment.home.HomeFragmentCustomer
import com.example.graduationproject.ui.mainActivityCustomer.fragment.NotificationsFragmentCustomer
import com.example.graduationproject.ui.mainActivityCustomer.fragment.chatFragment.ChatFragmentCustomer
import com.example.graduationproject.ui.mainActivityCustomer.fragment.SearchFragmentCustomer
import com.example.graduationproject.ui.mainActivityCustomer.fragment.home.CreatPostActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityCustomer : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    lateinit var viewBinding: ActivityMainCustomerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainCustomerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        bottomNavigationView = findViewById(R.id.bottomNavigationViewCustomer)

        val HomeFragmentCustomer = HomeFragmentCustomer()
        val SearchFragmentCustomer = SearchFragmentCustomer()
        val NotificationsFragmentCustomer = NotificationsFragmentCustomer()
        val ChatFragmentCustomer = ChatFragmentCustomer()
        setCurrentFragment(HomeFragmentCustomer)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> setCurrentFragment(HomeFragmentCustomer)
                R.id.navigation_search -> setCurrentFragment(SearchFragmentCustomer)
                R.id.navigation_notification -> setCurrentFragment(NotificationsFragmentCustomer)
                R.id.navigation_chat -> setCurrentFragment(ChatFragmentCustomer)
            }
            true
        }

//        onClickAddPost()
    }

//    private fun onClickAddPost() {
//        viewBinding.addButton.setOnClickListener {
//            val intent = Intent(this, CreatPostActivity::class.java)
//            startActivity(intent)
//        }
//    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }


}