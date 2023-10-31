package com.example.graduationproject.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.WebServices
import com.example.graduationproject.api.model.RegisterResponse
import com.example.graduationproject.databinding.ActivityRegisterBinding
import com.example.graduationproject.ui.LoginActivity
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityRegisterBinding
    private lateinit var viewModelRegister: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)
        viewModelRegister = RegisterViewModel()

        ifLogin()
        onClickRegister()
    }

    private fun performRegister() {
        val userName = viewBinding.userName.editText.toString()
        val email = viewBinding.email.editText.toString()
        val password = viewBinding.password.editText.toString()
        val phoneNumber = viewBinding.number.editText.toString()
        val password_confirmation = viewBinding.confirmPassword.editText.toString()

        viewModelRegister.performRegister(userName, email, password, phoneNumber, password_confirmation) { success, message ->
            if (success) {
                Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
                navigateToLogin()
            } else {
                Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun ifLogin() {
        viewBinding.loginNow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun onClickRegister() {
        viewBinding.registerBtn.setOnClickListener {
            performRegister()
        }
    }

    fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}