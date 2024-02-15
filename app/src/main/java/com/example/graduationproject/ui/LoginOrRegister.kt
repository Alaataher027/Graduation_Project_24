package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.databinding.ActivityLoginOrRegisterBinding
import com.example.graduationproject.ui.login.LoginActivity
import com.example.graduationproject.ui.mainActivityCustomer.MainActivityCustomer
import com.example.graduationproject.ui.register.RegisterActivity

class LoginOrRegister : AppCompatActivity() {

    private lateinit var viewBinding: ActivityLoginOrRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginOrRegisterBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        viewBinding.registerBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        viewBinding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        viewBinding.guest.setOnClickListener {
            val intent = Intent(this, MainActivityCustomer::class.java)
            startActivity(intent)
        }
    }


}
