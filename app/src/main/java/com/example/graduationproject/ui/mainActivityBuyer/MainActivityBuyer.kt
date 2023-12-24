package com.example.graduationproject.ui.mainActivityBuyer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.ui.mainActivityBuyer.fragment.HomeFragmentBuyer
import com.example.graduationproject.ui.mainActivityBuyer.fragment.NotificationsFragmentBuyer
import com.example.graduationproject.ui.mainActivityBuyer.fragment.profileFragment.ProfileFragmentBuyer
import com.example.graduationproject.ui.mainActivityBuyer.fragment.SearchFragmentBuyer
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityBuyer : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val HomeFragmentBuyer = HomeFragmentBuyer()
        val SearchFragmentBuyer = SearchFragmentBuyer()
        val NotificationsFragmentBuyer = NotificationsFragmentBuyer()
        val ProfileFragmentBuyer = ProfileFragmentBuyer()
        setCurrentFragment(HomeFragmentBuyer)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> setCurrentFragment(HomeFragmentBuyer)
                R.id.navigation_search -> setCurrentFragment(SearchFragmentBuyer)
                R.id.navigation_notification -> setCurrentFragment(NotificationsFragmentBuyer)
                R.id.navigation_profile -> setCurrentFragment(ProfileFragmentBuyer)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }
}