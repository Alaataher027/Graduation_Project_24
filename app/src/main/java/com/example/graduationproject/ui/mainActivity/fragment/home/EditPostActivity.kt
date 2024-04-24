package com.example.graduationproject.ui.mainActivity.fragment.home

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.graduationproject.api.model.post.postHome.DataItem
import com.example.graduationproject.databinding.ActivityEditPostBinding
import com.example.graduationproject.ui.login.TokenManager
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class EditPostActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityEditPostBinding
    private lateinit var viewModel: HomePostViewModel
    private lateinit var userDataViewModel: UserDataHomeViewModel
    private lateinit var tokenManager: TokenManager
    private var postData: DataItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        tokenManager = TokenManager(this)
        viewModel = HomePostViewModel(tokenManager)
        userDataViewModel = UserDataHomeViewModel()

        postData = intent.getParcelableExtra<DataItem>("POST_DATA")

        // Receive data from intent
        val postData = intent.getParcelableExtra<DataItem>("POST_DATA")

        // Populate views with post data
        populateViews(postData)

        // Call setupViews to initiate the editing process
        setupViews(null) // Pass null initially, since there's no image chosen yet
        onClickBack()
    }

    private fun onClickBack() {
        viewBinding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
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
        val postId = tokenManager.getPostId()

        // If imageUri is not null, load the image
        imageUri?.let {
            Log.d("EditPost", "**")
            viewBinding.addImage.setImageURI(it)
        }

        // Set up the click listener for adding or changing the image
        viewBinding.addImage.setOnClickListener {
            openGalleryForImage()
        }

        viewBinding.saveBtn.setOnClickListener {
            val description = RequestBody.create(
                MediaType.parse("text/plain"),
                viewBinding.descriptionContent.text.toString()
            )
            val quantity = RequestBody.create(
                MediaType.parse("text/plain"),
                viewBinding.quantityContent.text.toString()
            )
            val material = RequestBody.create(
                MediaType.parse("text/plain"),
                viewBinding.materialContent.text.toString()
            )
            val price = RequestBody.create(
                MediaType.parse("text/plain"),
                viewBinding.priceContent.text.toString()
            )

            if (imageUri == null) {
                if (viewBinding.addImage.drawable != null) {
                    val drawable = viewBinding.addImage.drawable as BitmapDrawable
                    val bitmap = drawable.bitmap
                    val imageFile = File.createTempFile("temp_image", ".jpg", applicationContext.cacheDir)
                    imageFile.outputStream().use {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    }
                    // Create a request body from the image file
                    val requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile)

                    // Create a MultipartBody.Part instance
                    val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

                    // Process other data to edit the post
                    viewModel.editPost(
                        accessToken,
                        postId,
                        description,
                        quantity,
                        material,
                        price,
                        body,
                        onSuccess = { message ->
                            // Show a toast for success
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            finish()
                        },
                        onError = { errorMessage ->
                            // Show a toast for error
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    // If there's no image loaded in viewBinding.addImage, show a toast indicating that an image is required
                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                }
                return@setOnClickListener
            }

            // Process the image and other data to edit the post
            val inputStream = contentResolver.openInputStream(imageUri)
            val requestFile =
                RequestBody.create(MediaType.parse("image/*"), inputStream!!.readBytes())
            val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)
            viewModel.editPost(
                accessToken,
                postId,
                description,
                quantity,
                material,
                price,
                body,
                onSuccess = { message ->
                    // Show a toast for success
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    finish()
                },
                onError = { errorMessage ->
                    // Show a toast for error
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }

    }


    private fun populateViews(postData: DataItem?) {
        postData?.let { post ->
            // Populate views with post data
            if (!isDestroyed) { // Check if activity is not destroyed
                Glide.with(this)
                    .load(post.image)
                    .into(viewBinding.addImage)
            }
            viewBinding.descriptionContent.setText(post.description)
            viewBinding.quantityContent.setText(post.quantity)
            viewBinding.materialContent.setText(post.material)
            viewBinding.priceContent.setText(post.price)

            // Fetch user data
            post.userId?.let { userId ->
                userDataViewModel.getData(
                    tokenManager.getToken() ?: "",
                    userId,
                    { userData ->
                        // Populate user data views
                        userData?.let {
                            if (!isDestroyed) { // Check if activity is not destroyed
                                viewBinding.userName.text = it.name
                                Glide.with(this)
                                    .load(it.image)
                                    .into(viewBinding.userImage)
                            }
                        }
                    },
                    { error ->
                        // Handle error
                        // For example, show error message or log error
                        // Log.e("EditPostActivity", "Failed to fetch user data: $error")
                    }
                )
            }
        }
    }

}