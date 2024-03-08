package com.example.graduationproject.ui.listActivityCustomer.ListComponents

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityListCustomerBinding
import com.example.graduationproject.databinding.DialogLogoutBinding
import com.example.graduationproject.ui.login.LoginActivity
import com.example.graduationproject.ui.logOut.LogOutViewModel
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.listActivityCustomer.ListComponents.material.MaterialsActivity
import com.example.graduationproject.ui.listActivityCustomer.ListComponents.profile.profileView.CustomerProfileActivity

class CustomerListActivity : AppCompatActivity() {

    private lateinit var viewModel: CustomerListViewModel
    private lateinit var binding: ActivityListCustomerBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(CustomerListViewModel::class.java)

        tokenManager = TokenManager(this)


        loadData()
        onClickBack()
        showDialogOnClickLogout()
        onClickProfile()
        navToMaterial()
    }

    private fun showDialogOnClickLogout() {
        binding.logoutBtn.setOnClickListener {
            val dialogBinding = DialogLogoutBinding.inflate(layoutInflater)
            val alertDialogBuilder = AlertDialog.Builder(this)
                .setView(dialogBinding.root)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            dialogBinding.option1Button.setOnClickListener {
                alertDialog.dismiss() // Dismiss the dialog if needed
                // Handle Option 1 click
                performLogout()

            }

            dialogBinding.option2Button.setOnClickListener {
                alertDialog.dismiss() // Dismiss the dialog if needed
            }

        }
    }

    private fun loadData() {
        val accessToken = tokenManager.getToken()
        if (accessToken != null) {
            viewModel.viewData(accessToken,
                onDataLoaded = { data ->
                    data?.let {
                        binding.nameUser.text = it.name
                        val requestOptions = RequestOptions().transform(CircleCrop())

                        Glide.with(this)
                            .load(data.image)
                            .apply(requestOptions)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error)
                            .into(binding.imageProfile)
                    }
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        } else {
            Toast.makeText(this, "Access token is null", Toast.LENGTH_SHORT).show()
        }
    }


    private fun performLogout() {
        val accessToken = tokenManager.getToken()
        if (accessToken != null) {
            val logOutViewModel =
                LogOutViewModel(tokenManager) // Initialize LogOutViewModel with tokenManager
            logOutViewModel.performLogOut(accessToken) { isSuccess, message ->
                if (isSuccess) {
                    // Clear token and finish activity
                    tokenManager.clearToken()
                    finishAffinity() // Finish all activities in the task associated with this activity
                    navToLogin()
                } else {
                    Toast.makeText(this@CustomerListActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Handle case where access token is null
            Toast.makeText(this@CustomerListActivity, "Access token is null", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun onClickProfile() {
        binding.profileBtn.setOnClickListener {
            val intent = Intent(this, CustomerProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onClickBack() {
        binding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }

    private fun navToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Optional: Close the current activity to prevent user from going back to it after logout
    }

    private fun navToMaterial() {
        binding.materialBtn.setOnClickListener {
            val intent = Intent(this, MaterialsActivity::class.java)
            startActivity(intent)
        }
    }
}
