package com.example.graduationproject.ui.mainActivitySeller.ListComponents.profile.profileView

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
import com.example.graduationproject.ui.mainActivitySeller.ListComponents.profile.editProfile.EditProfileSeller


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
        if (accessToken != null) {
            viewModel.viewData(accessToken,
                onDataLoaded = { data ->
                    data?.let {
                        viewBinding.emailSeller.text = it.email
                        viewBinding.phoneSellertextView.text = it.phoneNumber
                        viewBinding.governorateSeller.text = it.governorate
                        viewBinding.citySeller.text = it.city
                        viewBinding.streetSeller.text = it.street
                        viewBinding.quarterSeller.text = it.residentialQuarter

                        val requestOptions = RequestOptions().transform(CircleCrop())

                        Glide.with(this)
                            .load(data.image)
                            .apply(requestOptions)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error)
                            .into(viewBinding.imageProfile)
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
