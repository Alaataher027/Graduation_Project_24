package com.example.graduationproject.ui.listActivitySeller.ListComponents

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
import com.example.graduationproject.SavedPostsActivity
import com.example.graduationproject.databinding.ActivityListSellerBinding
import com.example.graduationproject.databinding.DialogLogoutBinding
import com.example.graduationproject.ui.PrivecyActivity
import com.example.graduationproject.ui.login.LoginActivity
import com.example.graduationproject.ui.logOut.LogOutViewModel
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.listActivityCustomer.ListComponents.material.MaterialsActivity
import com.example.graduationproject.ui.listActivitySeller.ListComponents.profile.profileView.SellerProfileActivity

class SellerListActivity : AppCompatActivity() {
    private lateinit var viewModel: SellerListViewModel
    private lateinit var binding: ActivityListSellerBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SellerListViewModel::class.java)

        tokenManager = TokenManager(this)

        // Set an OnClickListener for the ImageView using view binding
        onClickBack()
        showDialogOnClickLogout()
        onClickProfile()
        buttonBG()

        onClickSavedPosts()

//        loadData()
        onClickPrivecyBtn()
    }

    private fun onClickPrivecyBtn() {
        binding.privacyBtn.setOnClickListener {
            intent = Intent(this, PrivecyActivity::class.java)
            startActivity(intent)
        }
    }


    private fun onClickSavedPosts() {
        binding.savedBtn.setOnClickListener {
            intent = Intent(this, SavedPostsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun buttonBG() {
        val profileBtn = binding.profileBtn
        profileBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        profileBtn.backgroundTintList = null

        val savedPostsBtn = binding.savedBtn
        savedPostsBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        savedPostsBtn.backgroundTintList = null

        val privacyBtn = binding.privacyBtn
        privacyBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        privacyBtn.backgroundTintList = null

        val aboutUSBtn = binding.aboutusBtn
        aboutUSBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        aboutUSBtn.backgroundTintList = null

        val settingsBtn = binding.settingsBtn
        settingsBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        settingsBtn.backgroundTintList = null

        val guidanceBtn = binding.guidBtn
        guidanceBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        guidanceBtn.backgroundTintList = null

//        val materialBtn = binding.materialBtn
//        materialBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
//        materialBtn.backgroundTintList = null

        val TecBtn = binding.techBtn
        TecBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        TecBtn.backgroundTintList = null

        val ordersBtn = binding.orderBtn
        ordersBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        ordersBtn.backgroundTintList = null

        val logOutBtn = binding.logoutBtn
        logOutBtn.setBackgroundResource(R.drawable.rectangle_btn_list)
        logOutBtn.backgroundTintList = null

    }


    private fun showDialogOnClickLogout() {
        binding.logoutBtn.setOnClickListener {
            val dialogBinding = DialogLogoutBinding.inflate(layoutInflater)
            val alertDialogBuilder = AlertDialog.Builder(this)
                .setTitle("")
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

//    private fun loadData() {
//        val accessToken = tokenManager.getToken()
//        val userId = tokenManager.getUserId()
//
//        if (accessToken != null) {
//            viewModel.viewData(accessToken,userId,
//                onDataLoaded = { data ->
//                    data?.let {
//                        binding.nameUser.text = it.name
//
//                        val requestOptions = RequestOptions().transform(CircleCrop())
//
//                        if (!isDestroyed) { // Check if the activity is destroyed
//                            Glide.with(this)
//                                .load(data.image)
//                                .apply(requestOptions)
//                                .placeholder(R.drawable.placeholder)
//                                .error(R.drawable.error)
//                                .into(binding.imageProfile)
//                        }
//                    }
//                },
//                onError = { errorMessage ->
//                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//                }
//            )
//        } else {
//            Toast.makeText(this, "Access token is null", Toast.LENGTH_SHORT).show()
//        }
//    }


    private fun onClickBack() {
        binding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }

    private fun onClickProfile() {
        binding.profileBtn.setOnClickListener {
            val intent = Intent(this, SellerProfileActivity::class.java)
            startActivity(intent)
        }
    }


    private fun navToLogin() {
        startActivity(Intent(this@SellerListActivity, LoginActivity::class.java))
    }

//    private fun navToMaterial() {
//        binding.materialBtn.setOnClickListener {
//            val intent = Intent(this, MaterialsActivity::class.java)
//            startActivity(intent)
//        }
//    }


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
                    Toast.makeText(this@SellerListActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Handle case where access token is null
            Toast.makeText(this@SellerListActivity, "Access token is null", Toast.LENGTH_SHORT)
                .show()
        }
    }

}