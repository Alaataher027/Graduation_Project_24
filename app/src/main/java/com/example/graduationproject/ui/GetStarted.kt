package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivityGetStartedBinding

class GetStarted : AppCompatActivity() {

    private lateinit var viewBinding: ActivityGetStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityGetStartedBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        viewBinding.startBtn.setOnClickListener {
            val intent = Intent(this, LoginOrRegister::class.java)
            startActivity(intent)
        }
    }
}
