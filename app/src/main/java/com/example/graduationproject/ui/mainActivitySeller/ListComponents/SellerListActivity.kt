package com.example.graduationproject.ui.mainActivitySeller.ListComponents

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivityListSellerBinding
import com.example.graduationproject.ui.login.LoginActivity
import com.example.graduationproject.ui.mainActivityCustomer.ListComponents.material.MaterialsActivity
import com.example.graduationproject.ui.mainActivityCustomer.ListComponents.profile.CustomerProfileActivity

class SellerListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListSellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set an OnClickListener for the ImageView using view binding
        binding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }

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
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

//    private fun performLogout() {
//        logOutViewModel.performLogOut { success, message ->
//            if (success) {
//                Toast.makeText(this@BuyerListActivity, message, Toast.LENGTH_SHORT).show()
//                navToLogin()
//            } else {
//                Toast.makeText(this@BuyerListActivity, message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    private fun navToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun navToMaterial() {
        binding.materialBtn.setOnClickListener {
            val intent = Intent(this, MaterialsActivity::class.java)
            startActivity(intent)
        }
    }
}