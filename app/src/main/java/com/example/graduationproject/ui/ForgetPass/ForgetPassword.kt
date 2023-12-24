package com.example.graduationproject.ui.ForgetPass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityForgetPasswordBinding
import com.example.graduationproject.ui.OtpVertification
import com.example.graduationproject.ui.login.LoginActivity

class ForgetPassword : AppCompatActivity() {
    lateinit var viewBinding: ActivityForgetPasswordBinding
    lateinit var viewModel: ForgetPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        viewBinding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)
        viewModel = ForgetPasswordViewModel()

        onClickSendCode()
        rememberPass()
    }

    private fun rememberPass() {
        viewBinding.loginNow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onClickSendCode() {
        viewBinding.sendCodeBtn.setOnClickListener {
            sendCode()
        }
    }

    private fun sendCode() {
        val email = viewBinding.email.editText?.text.toString()

        viewModel.sendCode(email, "ar") { success, message ->
            if (success) {
                Toast.makeText(this@ForgetPassword, message, Toast.LENGTH_SHORT).show()
                navToOTP()

            } else {
                Toast.makeText(this@ForgetPassword, message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun navToOTP() {
        val intent = Intent(this, OtpVertification::class.java)
        startActivity(intent)
    }
}