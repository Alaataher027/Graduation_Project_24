package com.example.graduationproject.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.graduationproject.databinding.ActivityRegisterBinding
import com.example.graduationproject.ui.login.LoginActivity

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
        val name = viewBinding.userName.editText?.text.toString()
        val email = viewBinding.email.editText?.text.toString()
        val password = viewBinding.password.editText?.text.toString()
        val password_confirmation = viewBinding.confirmPassword.editText?.text.toString()
        val phoneNumber = viewBinding.number.editText?.text.toString()
        var user_type:String = "DefaultUserType"
        viewBinding.radioSeller.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                user_type = "Seller"
            }
        }

        viewBinding.radioBuyer.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                user_type  = "Buyer"
            }
        }


        viewModelRegister.performRegister(name, email, password,password_confirmation , phoneNumber, user_type ,"ar") { success, message ->
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