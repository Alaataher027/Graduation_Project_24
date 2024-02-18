package com.example.graduationproject.ui.mainActivitySeller.ListComponents.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityCustomerProfileBinding
import com.example.graduationproject.databinding.ActivitySellerProfileBinding
import com.example.graduationproject.ui.mainActivityCustomer.ListComponents.profile.EditProfileCustomer

class SellerProfileActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivitySellerProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySellerProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        onClickBackBtn()
        navigateToEditProfile()
    }

    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }

    private fun navigateToEditProfile() {
        viewBinding.editBtn.setOnClickListener() {
            val intent = Intent(this, EditProfileSeller::class.java)
            startActivity(intent)
        }
    }


}