package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityCreateNewPasswordBinding
import com.example.graduationproject.databinding.ActivityForgetPasswordBinding

class ForgetPassword : AppCompatActivity() {
    lateinit var viewBinding: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        viewBinding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        sendCode()
        rememberPass()
    }

    private fun rememberPass() {
        viewBinding.loginNow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun sendCode() {
        viewBinding.sendCodeBtn.setOnClickListener{
            val intent = Intent(this, OtpVertification::class.java)
            startActivity(intent)
        }
    }
}