package com.example.graduationproject.ui.mainActivity.fragment.home

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.graduationproject.api.model.post.PostResponse
import com.example.graduationproject.databinding.ActivityCreatPostBinding
import com.example.graduationproject.ui.login.TokenManager
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class CreatPostActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCreatPostBinding
    private lateinit var viewModel: CreatePostViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreatPostBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        tokenManager = TokenManager(this)
        viewModel = ViewModelProvider(this).get(CreatePostViewModel::class.java)

        setupViews(null)
        onClickBack()

        viewBinding.addImage.setOnClickListener {
            openGalleryForImage()
        }
    }


    private val requestImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                setupViews(it) // Pass the obtained Uri directly to the uploadImage function
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
                val description = viewBinding.descriptionContent.text.toString()
                val quantity = viewBinding.quantityContent.text.toString()
                val material = viewBinding.materialContent.text.toString()
                val price = viewBinding.priceContent.text.toString()

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
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
