package com.example.graduationproject.ui.mainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityMainBinding
import com.example.graduationproject.ui.mainActivity.fragment.home.HomeFragment
import com.example.graduationproject.ui.mainActivity.fragment.NotificationsFragment
import com.example.graduationproject.ui.mainActivity.fragment.chatFragment.ChatFragment
import com.example.graduationproject.ui.mainActivity.fragment.SearchFragment
import com.example.graduationproject.ui.mainActivity.fragment.home.CreatPostActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val HomeFragment = HomeFragment()
        val SearchFragment = SearchFragment()
        val NotificationsFragment = NotificationsFragment()
        val ChatFragment = ChatFragment()
        setCurrentFragment(HomeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> setCurrentFragment(HomeFragment)
                R.id.navigation_search -> setCurrentFragment(SearchFragment)
                R.id.navigation_notification -> setCurrentFragment(NotificationsFragment)
                R.id.navigation_chat -> setCurrentFragment(ChatFragment)
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