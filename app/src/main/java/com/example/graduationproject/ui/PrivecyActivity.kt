package com.example.graduationproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityPrivecyBinding
import com.example.graduationproject.databinding.ActivitySavedBinding

class PrivecyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivecyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivecyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }

    }
}