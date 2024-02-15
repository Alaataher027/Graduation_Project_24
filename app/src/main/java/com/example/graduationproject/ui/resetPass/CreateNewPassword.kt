package com.example.graduationproject.ui.resetPass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.graduationproject.databinding.ActivityCreateNewPasswordBinding
import com.example.graduationproject.ui.PasswordChanged

class CreateNewPassword : AppCompatActivity() {
    private lateinit var viewBinding: ActivityCreateNewPasswordBinding
    private lateinit var viewModel: CreateNewPasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        viewModel = CreateNewPasswordViewModel()

        onClickResetPass()
    }

    private fun onClickResetPass() {
        viewBinding.resetPasswordBtn.setOnClickListener {
            resetPassword()
        }
    }


    private fun resetPassword() {
        val email = viewBinding.email.editText?.text.toString()
        val newPass = viewBinding.newPassword.editText?.text.toString()
        val confirmPass = viewBinding.confirmPassword.editText?.text.toString()

        viewModel.resetPassword(email, newPass, confirmPass, "ar") { success, message ->
            if (success) {
                Toast.makeText(this@CreateNewPassword, message, Toast.LENGTH_SHORT).show()
                navToPasswordChanged()

            } else {
                Toast.makeText(this@CreateNewPassword, message, Toast.LENGTH_SHORT).show()
            }
        }
//        if (newPass != confirmPass) {
//            viewBinding.newPassword.error = "Password does not match!"
//            viewBinding.confirmPassword.error = "Password does not match!"
//
//        } else {
//            val intent = Intent(this, PasswordChanged::class.java)
//            startActivity(intent)
//        }


    }

    private fun navToPasswordChanged() {
        val intent = Intent(this, PasswordChanged::class.java)
        startActivity(intent)
    }
}