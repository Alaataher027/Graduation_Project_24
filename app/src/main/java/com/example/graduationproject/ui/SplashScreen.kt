package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.bumptech.glide.Glide
import com.example.graduationproject.ui.mainActivityCustomer.MainActivityCustomer
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivitySplashScreenBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivitySeller.MainActivitySeller

class SplashScreen : AppCompatActivity() {

    private val SPLASH_DELAY = 2000 // 2 seconds
    private lateinit var viewBinding: ActivitySplashScreenBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        tokenManager = TokenManager(this)

        val imageView = viewBinding.image
        Glide.with(this)
            .load(R.drawable.loogo)
            .into(imageView)

        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthentication()
        }, SPLASH_DELAY.toLong())

//        Handler().postDelayed({
//            checkAuthentication()
//        }, SPLASH_DELAY.toLong())
    }



    private fun checkAuthentication() {
        val userType = tokenManager.getUserType()
        Log.d("SplashScreen", "Retrieved User Type: $userType")

        if (tokenManager.getToken().isNullOrBlank() || userType.isNullOrBlank()) {
            // No token or user type, navigate to GetStarted activity
            val intent = Intent(this, GetStarted::class.java)
            startActivity(intent)
        } else {
            // Token and user type exist
            if (userType == "Seller") {

                // Navigate to SellerActivity
                val intent = Intent(this, MainActivitySeller::class.java)
                startActivity(intent)
            } else if (userType == "Customer"){
                // Navigate to BuyerActivity
                val intent = Intent(this, MainActivityCustomer::class.java)
                startActivity(intent)
            }
        }
        finish() // Close the splash activity
    }

    //    private fun checkAuthentication() {
//        if (tokenManager.getToken().isNullOrBlank()) {
//            // No token, navigate to GetStarted activity
//            val intent = Intent(this, GetStarted::class.java)
//            startActivity(intent)
//        } else {
//            // Token exists, navigate to MainActivity
//            val intent = Intent(this, MainActivityBuyer::class.java)
//            startActivity(intent)
//        }
//
//        finish() // Close the splash activity
//    }
}
