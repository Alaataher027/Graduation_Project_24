package com.example.graduationproject.ui.mainActivitySeller.ListComponents.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityEditProfileSellerBinding
import com.example.graduationproject.databinding.ActivitySellerProfileBinding

class EditProfileSeller : AppCompatActivity() {
    lateinit var viewBinding: ActivityEditProfileSellerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditProfileSellerBinding.inflate(layoutInflater)
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