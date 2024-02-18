package com.example.graduationproject.ui.mainActivityCustomer.ListComponents.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivityCustomerProfileBinding

class CustomerProfileActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityCustomerProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCustomerProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        navigateToEditProfile()
        onClickBackBtn()
    }

        private fun navigateToEditProfile() {
        // Use Intent to navigate to EditProfileActivity
        viewBinding.editBtn.setOnClickListener() {
            val intent = Intent(this, EditProfileCustomer::class.java)
            startActivity(intent)
        }
    }

    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }
}