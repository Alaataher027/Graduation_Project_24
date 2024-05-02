package com.example.graduationproject.ui.listActivityCustomer.ListComponents.profile.profileView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityCustomerProfileBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.listActivityCustomer.ListComponents.profile.editProfile.EditProfileCustomer
import com.example.graduationproject.ui.postProfile.PostsProfileActivity

class CustomerProfileActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityCustomerProfileBinding
    lateinit var viewModel: CustomerProfileViewModel
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCustomerProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewModel = ViewModelProvider(this).get(CustomerProfileViewModel::class.java)


        tokenManager = TokenManager(this)


        navigateToEditProfile()
        onClickBackBtn()
        loadData()
        onClickRefresh()
        navigateToUserPosts()
    }

    private fun navigateToUserPosts() {
        // Use Intent to navigate to EditProfileActivity
        viewBinding.postsBtn.setOnClickListener() {
            val intent = Intent(this, PostsProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onClickRefresh() {
        viewBinding.refreshBtn.setOnClickListener {
            loadData()
        }
    }


    private fun navigateToEditProfile() {
        // Use Intent to navigate to EditProfileActivity
        viewBinding.editBtn.setOnClickListener() {
            val intent = Intent(this, EditProfileCustomer::class.java)
            startActivity(intent)
        }
    }

    private fun loadData() {
        val accessToken = tokenManager.getToken()
        val userId = tokenManager.getUserId()

        if (accessToken != null) {
            viewModel.viewData(accessToken, userId,
                onDataLoaded = { data ->
                    data?.let {
                        viewBinding.emailCustomer.text = it.email
                        viewBinding.phoneCustomer.text = it.phoneNumber
                        viewBinding.governorateCustomer.text = it.governorate
                        viewBinding.cityCustomer.text = it.city
                        viewBinding.taxCustomer.text = it.tIN
                        viewBinding.nameUser.text = it.name
                        val requestOptions = RequestOptions().transform(CircleCrop())
                        if (!isDestroyed) { // Check if the activity is destroyed
                            Glide.with(this)
                                .load(data.image)
                                .apply(requestOptions)
                                .placeholder(R.drawable.placeholder)
                                .error(R.drawable.error)
                                .into(viewBinding.imageProfile)
                        }

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

    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }
}