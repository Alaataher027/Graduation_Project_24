package com.example.graduationproject.ui.listActivitySeller.ListComponents.profile.profileView

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivitySellerProfileBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.listActivitySeller.ListComponents.profile.editProfile.EditProfileSeller
import com.example.graduationproject.ui.postProfile.PostsProfileActivity


class SellerProfileActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivitySellerProfileBinding
    private lateinit var viewModel: SellerProfileViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySellerProfileBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewModel = ViewModelProvider(this).get(SellerProfileViewModel::class.java)
        tokenManager = TokenManager(this)

        onClickBackBtn()
        navigateToEditProfile()
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


    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun navigateToEditProfile() {
        viewBinding.editBtn.setOnClickListener {
            val intent = Intent(this, EditProfileSeller::class.java)
            startActivity(intent)
        }
    }

    private fun loadData() {
        val accessToken = tokenManager.getToken()
        val userId = tokenManager.getUserId()

        if (accessToken != null) {
            viewModel.viewData(accessToken,userId,
                onDataLoaded = { data ->
                    data?.let {
                        viewBinding.emailSeller.text = it.email
                        viewBinding.phoneSellertextView.text = it.phoneNumber
                        viewBinding.governorateSeller.text = it.governorate
                        viewBinding.citySeller.text = it.city
                        viewBinding.streetSeller.text = it.street
                        viewBinding.quarterSeller.text = it.residentialQuarter
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
}
