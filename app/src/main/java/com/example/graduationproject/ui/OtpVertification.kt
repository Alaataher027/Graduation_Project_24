package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityOtpVertificationBinding

class OtpVertification : AppCompatActivity() {
    lateinit var viewBinding:ActivityOtpVertificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityOtpVertificationBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)


        verifyPass()
    }

    private fun verifyPass() {
        viewBinding.verifyBtn.setOnClickListener{
            val intent = Intent(this, CreateNewPassword::class.java)
            startActivity(intent)
        }
    }
}