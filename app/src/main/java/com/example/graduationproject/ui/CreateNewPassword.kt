package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityCreateNewPasswordBinding

class CreateNewPassword : AppCompatActivity() {
    private lateinit var viewBinding : ActivityCreateNewPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityCreateNewPasswordBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        reset_password()
    }

    private fun reset_password() {
        viewBinding.resetPasswordBtn.setOnClickListener{
            val newPass  = viewBinding.newPassword.editText?.text.toString()
            val confirmPass  = viewBinding.confirmPassword.editText?.text.toString()


            if(newPass != confirmPass){
                viewBinding.newPassword.error = "Password does not match!"
                viewBinding.confirmPassword.error = "Password does not match!"

            }
            else{
                val intent = Intent(this, PasswordChanged::class.java)
                startActivity(intent)
            }

        }
    }
}