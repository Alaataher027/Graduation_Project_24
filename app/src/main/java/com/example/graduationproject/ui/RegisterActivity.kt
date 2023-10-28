package com.example.graduationproject.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.graduationproject.R
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.WebServices
import com.example.graduationproject.api.model.RegisterResponse
import com.example.graduationproject.data.User
import com.example.graduationproject.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityRegisterBinding
    private lateinit var apiService: WebServices
//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)
        apiService = ApiManager.getApis()
        ifLogin()
        onClickRegister()
    }


    private fun performRegister() {


        val password = viewBinding.password.editText.toString()
        val userName = viewBinding.userName.editText.toString()
        val phoneNumber = viewBinding.number.editText.toString()
        val email = viewBinding.email.editText.toString()
        val user = User(
            userName = userName,
            email = email,
            password = password,
            phoneNumber = phoneNumber
        )

        ApiManager
            .getApis()
            .userRegister(userName, email, password, phoneNumber)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
//                    // Handle successful registration response
                        if (registerResponse != null) {
                            // Registration successful, do something
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registration successful",
                                Toast.LENGTH_SHORT
                            ).show()

                            navigateToLogin()
                        }
                    } else {
                        // Handle unsuccessful registration response
                        val errorResponse = response.errorBody()?.string()
                        if (errorResponse != null) {
                            try {
                                val errorJson = JSONObject(errorResponse)
                                val errorMessage = errorJson.getString("message")
                                val errorObject = errorJson.getJSONObject("errors")
                                val nameError = errorObject.getJSONArray("name").getString(0)
                                val emailError = errorObject.getJSONArray("email").getString(0)
                                val passwordError =
                                    errorObject.getJSONArray("password").getString(0)
                                val phoneNumberError =
                                    errorObject.getJSONArray("phone_number").getString(0)

                                // Display error messages
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration failed: $errorMessage\n" +
                                            "Name: $nameError\n" +
                                            "Email: $emailError\n" +
                                            "Password: $passwordError\n" +
                                            "Phone Number: $phoneNumberError",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration failed",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    // Handle network or API call failure
                    Log.e("RegisterActivity", "Registration failed", t)

                    // Show a user-friendly error message
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registration failed: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
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
//    private fun performRegistration(user:User) {
//        //val call = apiService.registUser(user)
//        ApiManager
//            .getApis()
//            .userRegister(user)
//            .enqueue(object : Callback<RegisterResponse> {
//            override fun onResponse(
//                call: Call<RegisterResponse>,
//                response: Response<RegisterResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val registerResponse = response.body()
//                    // Handle successful registration response
//                    if (registerResponse != null) {
//                        // Registration successful, do something
//                        Toast.makeText(
//                            this@RegisterActivity,
//                            "Registration successful",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    // Handle unsuccessful registration response
//
//                    val errorResponse = response.errorBody()?.string()
//                    if (errorResponse != null) {
//                        try {
//                            val errorJson = JSONObject(errorResponse)
//                            val errorMessage = errorJson.getString("message")
//                            val errorObject = errorJson.getJSONObject("errors")
//                            val nameError = errorObject.getJSONArray("name").getString(0)
//                            val emailError = errorObject.getJSONArray("email").getString(0)
//                            val passwordError = errorObject.getJSONArray("password").getString(0)
//                            val phoneNumberError = errorObject.getJSONArray("phone_number").getString(0)
//
//                            // Display error messages
//                            Toast.makeText(
//                                this@RegisterActivity,
//                                "Registration failed: $errorMessage\n" +
//                                        "Name: $nameError\n" +
//                                        "Email: $emailError\n" +
//                                        "Password: $passwordError\n" +
//                                        "Phone Number: $phoneNumberError",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            Toast.makeText(
//                                this@RegisterActivity,
//                                "Registration failed",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//            }
//            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                // Handle network or API call failure
//                Toast.makeText(
//                    this@RegisterActivity,
//                    "Registration failed",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        })
//    }


}
