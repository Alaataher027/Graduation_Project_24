package com.example.graduationproject.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.graduationproject.ui.mainActivity.MainActivity
import com.example.graduationproject.databinding.ActivityLoginBinding
import com.example.graduationproject.ui.ForgetPass.ForgetPassword
import com.example.graduationproject.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityLoginBinding
    private lateinit var viewModelLogin: LoginViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        // Initialize the TokenManager
        tokenManager = TokenManager(this)

        // Pass the TokenManager to the LoginViewModel
        viewModelLogin = LoginViewModel(tokenManager)

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

        viewModelLogin.performLogin(email, password, "ar") { success, message ->
            if (success) {
                Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                // Save the access token when login is successful
                tokenManager.getToken()?.let {
                    Log.d("LoginActivity", "Access Token: $it")
                }
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

    fun if_register() {
        viewBinding.registNow.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    fun navToHome() {
//        val userType = tokenManager.getUserType()
//        Log.d("LoginActivity", "Retrieved User Type: $userType")
//
//        if (userType == "Seller") {
//            // Navigate to SellerActivity
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        } else if (userType == "Customer") {
//            // Navigate to BuyerActivity
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

        finish()
    }


}