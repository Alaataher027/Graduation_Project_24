package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivitySplashScreenBinding


class SplashScreen : AppCompatActivity() {

    private val SPLASH_DELAY = 2000 // 2 seconds
    lateinit var viewBinding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        val imageView = viewBinding.image
        Glide.with(this)
            .load(R.drawable.loogo)
            .into(imageView)

        Handler().postDelayed({
            val intent = Intent(this, GetStarted::class.java)
            startActivity(intent)
            finish() // Close the splash activity
        }, SPLASH_DELAY.toLong())

    }
}

