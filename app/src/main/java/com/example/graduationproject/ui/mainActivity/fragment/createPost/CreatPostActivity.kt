package com.example.graduationproject.ui.mainActivity.fragment.createPost

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityCreatPostBinding
import com.example.graduationproject.ui.login.LoginViewModel
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


class CreatPostActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCreatPostBinding
    private lateinit var viewModel: CreatePostViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var viewModelProfile: UserDataHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreatPostBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        tokenManager = TokenManager(this)
        viewModel = CreatePostViewModel(tokenManager)
        viewModelProfile = UserDataHomeViewModel()

        onClickBack()

        viewBinding.addImage.setOnClickListener {
            openGalleryForImage()
        }

        viewUserData()
    }

//    private fun viewUserData() {
//        val accessToken = tokenManager.getToken() ?: ""
//        val userId = tokenManager.getUserId()
//
//        viewModelProfile.getData(accessToken, userId, { userData ->
//            // Populate user name
//            viewBinding.userName.text = userData?.name ?: ""
//
//            // Load user image using Glide
//            userData?.let { user ->
//                Glide.with(this@CreatPostActivity)
//                    .load(user.image)
//                    .placeholder(R.drawable.placeholder) // Placeholder image while loading
//                    .error(R.drawable.error) // Image to show in case of error
//                    .into(viewBinding.userImage)
//            }
//        }, { errorMessage ->
//            // Handle error if any
//            Log.e("UserDataError", errorMessage)
//        })
//    }

    private fun viewUserData() {
        val accessToken = tokenManager.getToken() ?: ""
        val userId = tokenManager.getUserId()

        viewModelProfile.getData(accessToken, userId, { userData ->
            // Populate user name
            viewBinding.userName.text = userData?.name ?: ""

            // Load user image using Glide only if the activity is not destroyed
            if (!isDestroyed) {
                userData?.let { user ->
                    Glide.with(this@CreatPostActivity)
                        .load(user.image)
                        .placeholder(R.drawable.placeholder) // Placeholder image while loading
                        .error(R.drawable.error) // Image to show in case of error
                        .into(viewBinding.userImage)
                }
            }
        }, { errorMessage ->
            // Handle error if any
            Log.e("UserDataError", errorMessage)
        })
    }



    private val requestImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                setupViews(it)
            }
        }

    private fun openGalleryForImage() {
        requestImageLauncher.launch("image/*")
    }
    private fun setupViews(imageUri: Uri?) {
        val accessToken = tokenManager.getToken() ?: ""

        // Now imageUri can be null, so you need to handle it appropriately
        imageUri?.let {
            viewBinding.addImage.setImageURI(it)
            val inputStream = contentResolver.openInputStream(imageUri)
            val requestFile =
                RequestBody.create(MediaType.parse("image/*"), inputStream!!.readBytes())
            val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

            viewBinding.shareBtn.setOnClickListener {
                val description = RequestBody.create(MediaType.parse("text/plain"), viewBinding.descriptionContent.text.toString())
                val quantity = RequestBody.create(MediaType.parse("text/plain"), viewBinding.quantityContent.text.toString())
                val material = RequestBody.create(MediaType.parse("text/plain"), viewBinding.materialContent.text.toString())
                val price = RequestBody.create(MediaType.parse("text/plain"), viewBinding.priceContent.text.toString())

                viewModel.createPost(
                    accessToken,
                    description,
                    quantity,
                    material,
                    price,
                    body
                ) { isSuccess, message ->
                    if (isSuccess) {
                        Log.d("CreatePost", "successful, ${imageUri}")
                        Log.d("CreatePost", "body:, ${body}")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.d("CreatePost", "imageUri:, ${imageUri}")
                        Log.d("CreatePost", "body:, ${body}")
                        Log.d("CreatePost", ", ${description}")
                        Log.d("CreatePost", ", ${quantity}")
                        Log.d("CreatePost", ", ${material}")
                        Log.d("CreatePost", ", ${price}")
                        Log.d("CreatePost", ", ${message}")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun onClickBack() {
        viewBinding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }
}
