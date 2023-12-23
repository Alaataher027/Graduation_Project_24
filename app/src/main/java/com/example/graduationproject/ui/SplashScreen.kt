package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.graduationproject.MainActivity
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivitySplashScreenBinding
import com.example.graduationproject.ui.login.TokenManager

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

        Handler().postDelayed({
            checkAuthentication()
        }, SPLASH_DELAY.toLong())
    }

    private fun checkAuthentication() {
        if (tokenManager.getToken().isNullOrBlank()) {
            // No token, navigate to GetStarted activity
            val intent = Intent(this, GetStarted::class.java)
            startActivity(intent)
        } else {
            // Token exists, navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        finish() // Close the splash activity
    }
}


