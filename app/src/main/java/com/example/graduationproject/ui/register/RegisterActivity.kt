package com.example.graduationproject.ui.register

import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.graduationproject.R
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

        val user_type = when {
            viewBinding.radioSeller.isChecked -> "Seller"
            viewBinding.radioCustomer.isChecked -> "Customer"
            else -> "DefaultUserType" // Set a default value if neither is checked
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

            val progressBar = viewBinding.progressBar
            val progressDrawable = progressBar.indeterminateDrawable.mutate()
            progressDrawable.setColorFilter(
                ContextCompat.getColor(this, R.color.my_light_primary),
                PorterDuff.Mode.SRC_IN
            )
            progressBar.indeterminateDrawable = progressDrawable
        }
    }

    fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}