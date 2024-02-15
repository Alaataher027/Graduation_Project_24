package com.example.graduationproject.ui.mainActivitySeller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityMainSellerBinding
import com.example.graduationproject.ui.mainActivitySeller.fragment.home.HomeFragmentSeller
import com.example.graduationproject.ui.mainActivityCustomer.fragment.home.CreatPostActivity
import com.example.graduationproject.ui.mainActivitySeller.fragment.ChatFragmentSeller
import com.example.graduationproject.ui.mainActivitySeller.fragment.NotificationsFragmentSeller
import com.example.graduationproject.ui.mainActivitySeller.fragment.SearchFragmentSeller
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivitySeller : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    lateinit var viewBinding: ActivityMainSellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainSellerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        bottomNavigationView = findViewById(R.id.bottomNavigationViewSeller)

        val HomeFragmentSeller = HomeFragmentSeller()
        val SearchFragmentSeller = SearchFragmentSeller()
        val NotificationsFragmentSeller = NotificationsFragmentSeller()
        val ChatFragmentSeller = ChatFragmentSeller()
        setCurrentFragment(HomeFragmentSeller)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> setCurrentFragment(HomeFragmentSeller)
                R.id.navigation_search -> setCurrentFragment(SearchFragmentSeller)
                R.id.navigation_notification -> setCurrentFragment(NotificationsFragmentSeller)
                R.id.navigation_chat -> setCurrentFragment(ChatFragmentSeller)
            }
            true
        }
        onClickAddPost()
    }

    private fun onClickAddPost() {
        viewBinding.addButton.setOnClickListener {
            val intent = Intent(this, CreatPostActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }
}