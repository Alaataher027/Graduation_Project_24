package com.example.graduationproject.ui.listActivityCustomer.ListComponents

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.graduationproject.AboutAsActivity
import com.example.graduationproject.GuideActivity
import com.example.graduationproject.R
import com.example.graduationproject.SavedPostsActivity
import com.example.graduationproject.databinding.ActivityListCustomerBinding
import com.example.graduationproject.databinding.DialogLogoutBinding
import com.example.graduationproject.ui.PrivecyActivity
import com.example.graduationproject.ui.login.LoginActivity
import com.example.graduationproject.ui.logOut.LogOutViewModel
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.listActivityCustomer.ListComponents.profile.profileView.CustomerProfileActivity
import com.example.graduationproject.ui.listActivityCustomer.ListComponents.ordersPending.OrdersActivity

class CustomerListActivity : AppCompatActivity() {

    private lateinit var viewModel: CustomerListViewModel
    private lateinit var binding: ActivityListCustomerBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(CustomerListViewModel::class.java)

        tokenManager = TokenManager(this)


        buttonBG()
        onClickSavedPosts()
        onClickBack()
        onClickAboutUS()
        onClickGuide()
        showDialogOnClickLogout()
        onClickProfile()
        onClickPrivecyBtn()
        onClickOrders()
        onClickSearch()
    }

    private fun onClickSearch() {

    }

    private fun onClickOrders() {
        binding.orderBtn.setOnClickListener {
            intent = Intent(this, OrdersActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onClickGuide() {
        binding.guidBtn.setOnClickListener {
            intent = Intent(this, GuideActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onClickPrivecyBtn() {
        binding.privacyBtn.setOnClickListener {
            intent = Intent(this, PrivecyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onClickSavedPosts() {
        binding.savedBtn.setOnClickListener {
            intent = Intent(this, SavedPostsActivity::class.java)
            startActivity(intent)
        }
    }


    private fun buttonBG() {
        val profileBtn = binding.profileBtn
        profileBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        profileBtn.backgroundTintList = null

        val savedPostsBtn = binding.savedBtn
        savedPostsBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        savedPostsBtn.backgroundTintList = null

        val privacyBtn = binding.privacyBtn
        privacyBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        privacyBtn.backgroundTintList = null

        val aboutUSBtn = binding.aboutusBtn
        aboutUSBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        aboutUSBtn.backgroundTintList = null

        val settingsBtn = binding.settingsBtn
        settingsBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        settingsBtn.backgroundTintList = null

        val guidanceBtn = binding.guidBtn
        guidanceBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        guidanceBtn.backgroundTintList = null

        val TecBtn = binding.techBtn
        TecBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        TecBtn.backgroundTintList = null

        val ordersBtn = binding.orderBtn
        ordersBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        ordersBtn.backgroundTintList = null

        val logOutBtn = binding.logoutBtn
        logOutBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        logOutBtn.backgroundTintList = null

    }

    private fun showDialogOnClickLogout() {
        binding.logoutBtn.setOnClickListener {
            val dialogBinding = DialogLogoutBinding.inflate(layoutInflater)
            val alertDialogBuilder = AlertDialog.Builder(this)
                .setView(dialogBinding.root)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            dialogBinding.option1Button.setOnClickListener {
                alertDialog.dismiss() // Dismiss the dialog if needed
                // Handle Option 1 click
                performLogout()

            }

            dialogBinding.option2Button.setOnClickListener {
                alertDialog.dismiss() // Dismiss the dialog if needed
            }

        }
    }


    private fun performLogout() {
        val accessToken = tokenManager.getToken()
        if (accessToken != null) {
            val logOutViewModel =
                LogOutViewModel(tokenManager) // Initialize LogOutViewModel with tokenManager
            logOutViewModel.performLogOut(accessToken) { isSuccess, message ->
                if (isSuccess) {
                    // Clear token and finish activity
                    tokenManager.clearToken()
                    finishAffinity() // Finish all activities in the task associated with this activity
                    navToLogin()
                } else {
                    Toast.makeText(this@CustomerListActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Handle case where access token is null
            Toast.makeText(this@CustomerListActivity, "Access token is null", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun onClickProfile() {
        binding.profileBtn.setOnClickListener {
            val intent = Intent(this, CustomerProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onClickAboutUS() {
        binding.aboutusBtn.setOnClickListener {
            val intent = Intent(this, AboutAsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onClickBack() {
        binding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }

    private fun navToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Optional: Close the current activity to prevent user from going back to it after logout
    }

}