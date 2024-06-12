package com.example.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivityAboutAsBinding

class AboutAsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutAsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }

    }
}