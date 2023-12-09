package com.example.graduationproject.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.graduationproject.MainActivity
import com.example.graduationproject.databinding.ActivityLoginBinding
import com.example.graduationproject.ui.ForgetPassword
import com.example.graduationproject.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLoginBinding
    private lateinit var viewModelLogin: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)
        viewModelLogin = LoginViewModel()
        onClickLogin()
        if_register()
        forgetPass()

    }

    private fun onClickLogin() {
        viewBinding.loginBtn.setOnClickListener {
            performLogin()
        }
    }


    private fun performLogin() {
        val email = viewBinding.email.editText?.text.toString()
        val password = viewBinding.password.editText?.text.toString()

        //Log.d("LoginActivity", "Email: $email, Password: $password")

        viewModelLogin.performLogin(email, password) { success, message ->
//            Log.d("LoginActivity", "Inside performLogin callback")
            if (success) {
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                navToHome()
            } else {
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun forgetPass() {
        viewBinding.forgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }
    }

    fun if_register(){
        viewBinding.registNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    fun navToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}