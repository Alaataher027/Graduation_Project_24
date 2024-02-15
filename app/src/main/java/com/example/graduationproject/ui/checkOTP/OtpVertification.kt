package com.example.graduationproject.ui.checkOTP

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.graduationproject.databinding.ActivityOtpVertificationBinding
import com.example.graduationproject.ui.resetPass.CreateNewPassword

class OtpVertification : AppCompatActivity() {
    lateinit var viewBinding:ActivityOtpVertificationBinding
    lateinit var viewModel: OtpVertificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityOtpVertificationBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)
        viewModel = OtpVertificationViewModel()

        onClickVerify()
    }

    private fun onClickVerify() {
        viewBinding.verifyBtn.setOnClickListener{
            checkCode()
        }
    }


    private fun checkCode() {
        val code = viewBinding.code.editText?.text.toString()
        viewModel.checkCode(code, "ar") { success, message ->
            if (success) {
                Toast.makeText(this@OtpVertification, message, Toast.LENGTH_SHORT).show()
                navToCreateNewPass()

            } else {
                Toast.makeText(this@OtpVertification, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navToCreateNewPass() {
        val intent = Intent(this, CreateNewPassword::class.java)
        startActivity(intent)
    }
}