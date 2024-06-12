package com.example.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivityGuideBinding
import com.example.graduationproject.databinding.ActivityPrivecyBinding

class GuideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }

    }
}