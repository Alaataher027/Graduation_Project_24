package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.graduationproject.ui.mainActivity.MainActivity
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivitySplashScreenBinding
import com.example.graduationproject.ui.login.TokenManager

class SplashScreen : AppCompatActivity() {

    private val SPLASH_DELAY = 2500 // 2 seconds
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
//            navigateToHome()
            checkAuthentication()
        }, SPLASH_DELAY.toLong())

//        Handler().postDelayed({
//            checkAuthentication()
//        }, SPLASH_DELAY.toLong())
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else if (userType == "Customer"){
                // Navigate to BuyerActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        finish() // Close the splash activity
    }
}
