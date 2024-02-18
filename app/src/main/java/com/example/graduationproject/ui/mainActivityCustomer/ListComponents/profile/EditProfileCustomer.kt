package com.example.graduationproject.ui.mainActivityCustomer.ListComponents.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityCustomerProfileBinding
import com.example.graduationproject.databinding.ActivityEditProfileCustomerBinding

class EditProfileCustomer : AppCompatActivity() {
    lateinit var viewBinding: ActivityEditProfileCustomerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditProfileCustomerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        onClickBackBtn()
    }

    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }
}