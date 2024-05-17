package com.example.graduationproject.ui.mainActivity.fragment.createPost

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
    private lateinit var materialPrice: MaterialPrice
    private var quantityValue = 1 // Initial value of quantity_content
    private lateinit var spinner: Spinner // Declare Spinner as class-level variable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreatPostBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        tokenManager = TokenManager(this)
        viewModel = CreatePostViewModel(tokenManager)
        viewModelProfile = UserDataHomeViewModel()

        materialPrice = MaterialPrice()

        onClickBack()

        viewBinding.addImage.setOnClickListener {
            openGalleryForImage()
        }

        viewUserData()
        shareBtnView()

        viewBinding.decBtn.setOnClickListener {
            decreaseQuantity()
        }

        viewBinding.incBtn.setOnClickListener {
            increaseQuantity()
        }

        // Initialize the spinner
        spinner = findViewById(R.id.quantity_spinner)

        ArrayAdapter.createFromResource(
            this,
            R.array.quantity_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selection here
                val selectedOption = parent.getItemAtPosition(position).toString()

                // Save the selected quantity type based on the selected option
                when (selectedOption) {
                    // Handle different options if needed
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }


    private fun decreaseQuantity() {
        // Check if the current value is greater than 1 to prevent negative values
        if (quantityValue > 1) {
            quantityValue-- // Decrease the value
            viewBinding.quantityContent.setText(quantityValue.toString()) // Update the EditText
        } else {
            // Show a toast or handle the case where the value cannot be decreased further
            Toast.makeText(this, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show()
        }
    }

    private fun increaseQuantity() {
        // Increase the value
        quantityValue++
        viewBinding.quantityContent.setText(quantityValue.toString()) // Update the EditText
    }


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

            // Check if inputStream is null before proceeding
            if (inputStream != null) {
                val requestFile =
                    RequestBody.create(MediaType.parse("image/*"), inputStream.readBytes())
                val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

                viewBinding.shareBtn.setOnClickListener {
                    val description = RequestBody.create(
                        MediaType.parse("text/plain"),
                        viewBinding.descriptionContent.text.toString()
                    )

                    // Concatenate spinner value with quantity content
                    var selectedQuantityShowed = ""
                    val selectedQuantity = spinner.selectedItem.toString()
                    val quantityValue = viewBinding.quantityContent.text.toString()
                    if (selectedQuantity == "Quantity (pieces)") {
                        selectedQuantityShowed = "Pieces"
                    } else {
                        selectedQuantityShowed = "Kilo"
                    }
                    val quantity = RequestBody.create(
                        MediaType.parse("text/plain"),
                        "$quantityValue $selectedQuantityShowed"
                    )

                    val price = RequestBody.create(
                        MediaType.parse("text/plain"),
                        viewBinding.priceContent.text.toString()
                    )

                    viewModel.createPost(
                        accessToken,
                        description,
                        quantity,
                        price,
                        body
                    ) { isSuccess, message ->
                        if (isSuccess) {
                            Log.d("CreatePost", "successful, ${imageUri}")
                            Log.d("CreatePost", "body:, ${body}")
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Log.d("CreatePostAc", "imageUri:, ${imageUri}")
                            Log.d("CreatePostAc", "body:, ${body}")
                            Log.d("CreatePostAc", ", ${description}")
                            Log.d("CreatePostAc", ", ${quantity}")
                            Log.d("CreatePostAc", ", ${price}")
                            Log.d("CreatePostAc", ", ${message}")
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // Handle case where inputStream is null
                Toast.makeText(this, "Failed to read image file", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun shareBtnView() {
        val profileBtn = viewBinding.shareBtn
        profileBtn.setBackgroundResource(R.drawable.rec_press)
        profileBtn.backgroundTintList = null
    }

    private fun onClickBack() {
        viewBinding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }
}
