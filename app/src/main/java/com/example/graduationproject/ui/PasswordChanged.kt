package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivityPasswordChangedBinding
import com.example.graduationproject.ui.login.LoginActivity

class PasswordChanged : AppCompatActivity() {
    lateinit var viewBinding: ActivityPasswordChangedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPasswordChangedBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        backToLogin()
    }

    private fun backToLogin() {
        viewBinding.backToLoginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }


}