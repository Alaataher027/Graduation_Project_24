package com.example.graduationproject.ui.mainActivityCustomer.ListComponents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.graduationproject.databinding.ActivityListCustomerBinding
import com.example.graduationproject.ui.login.LoginActivity
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivityCustomer.ListComponents.logOut.LogOutViewModel
import com.example.graduationproject.ui.mainActivityCustomer.ListComponents.logOut.SessionManager
import com.example.graduationproject.ui.mainActivityCustomer.ListComponents.material.MaterialsActivity
import com.example.graduationproject.ui.mainActivityCustomer.ListComponents.profile.CustomerProfileActivity

class CustomerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListCustomerBinding
    private val logOutViewModel: LogOutViewModel by viewModels()
//    private lateinit var tokenManager: TokenManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set an OnClickListener for the ImageView using view binding
        binding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
//        tokenManager = TokenManager(this)

        onClickLogOut()
        onClickProfile()
        navToMaterial()
    }

    private fun onClickProfile() {
        binding.profileBtn.setOnClickListener {
            val intent = Intent(this, CustomerProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onClickLogOut() {
        binding.logoutBtn.setOnClickListener {
            performLogout()
        }
    }

    private fun performLogout() {
        val accessToken = SessionManager.getToken(this@CustomerListActivity)
        if (accessToken != null) {
            LogOutViewModel().performLogOut(accessToken) { isSuccess, message ->
                if (isSuccess) {
                    Toast.makeText(this@CustomerListActivity, message, Toast.LENGTH_SHORT).show()
                    // Navigate to login screen after successful logout
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


    private fun navToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Optional: Close the current activity to prevent user from going back to it after logout
    }

    private fun navToMaterial() {
        binding.materialBtn.setOnClickListener {
            val intent = Intent(this, MaterialsActivity::class.java)
            startActivity(intent)
        }
    }
}
