package com.example.graduationproject.ui.mainActivity.fragment.home

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import android.os.Handler

class EditPostActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityEditPostBinding
    private lateinit var viewModel: HomePostViewModel
    private lateinit var userDataViewModel: UserDataHomeViewModel
    private lateinit var tokenManager: TokenManager
    private var postData: DataItem? = null

    private var quantity: Int = 1 // Default quantity

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
        setupQuantityButtons()
        observeMaterialClassification()
        setupMaterialSelection()
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
                classifyImage(it)
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
            val material = RequestBody.create(
                MediaType.parse("text/plain"),
                viewBinding.materialContent.text.toString()
            )
            val price = RequestBody.create(
                MediaType.parse("text/plain"),
                viewBinding.priceContent.text.toString() // Use priceContent value directly
            )

            if (imageUri == null) {
                if (viewBinding.addImage.drawable != null) {
                    val drawable = viewBinding.addImage.drawable as BitmapDrawable
                    val bitmap = drawable.bitmap
                    val imageFile =
                        File.createTempFile("temp_image", ".jpg", applicationContext.cacheDir)
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
                        RequestBody.create(MediaType.parse("text/plain"), quantity.toString()),
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
                RequestBody.create(MediaType.parse("text/plain"), quantity.toString()),
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
            // Load post data into views
            Glide.with(this)
                .load(post.image)
                .into(viewBinding.addImage)
            viewBinding.descriptionContent.setText(post.description)
            viewBinding.materialContent.setText(post.material)

            // تحديث كمية السلعة والسعر بناءً على البيانات المحملة
            quantity = post.quantity?.toIntOrNull() ?: 1
            updateQuantityAndPrice()


            // Fetch user data
            post.userId?.let { userId ->
                userDataViewModel.getData(
                    tokenManager.getToken() ?: "",
                    userId,
                    { userData ->
                        // Populate user data views
                        userData?.let {
                            viewBinding.userName.text = it.name
                            Glide.with(this)
                                .load(it.image)
                                .into(viewBinding.userImage)
                        }
                    },
                    { error ->
                        // Handle error
                        Log.e("EditPostActivity", "Failed to fetch user data: $error")
                    }
                )
            }
        }
    }

    private var isUpdatingText = false // تعريف متغير للتحقق من حدوث تحديث نصي مستمر

    private fun setupQuantityButtons() {
        viewBinding.incBtn.setOnClickListener {
            quantity++
            updateQuantityAndPrice()
        }

        viewBinding.decBtn.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantityAndPrice()
            }
        }

        viewBinding.quantityContent.setText(quantity.toString())

        // Listen for manual input changes
        viewBinding.quantityContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isUpdatingText) {
                    s?.toString()?.let {
                        val inputQuantity = it.toIntOrNull()
                        if (inputQuantity != null && inputQuantity >= 1) {
                            quantity = inputQuantity
                            updateQuantityAndPriceDelayed() // تحديث بعد فترة زمنية معينة
                        } else {
                            // Handle invalid input (optional)
                            Toast.makeText(
                                this@EditPostActivity,
                                "Please enter a valid quantity",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
    }

    // تحديث الكمية والسعر بعد فترة زمنية معينة
    private fun updateQuantityAndPriceDelayed() {
        if (!isUpdatingText) {
            isUpdatingText = true
            Handler().postDelayed({
                updateQuantityAndPrice()
                isUpdatingText = false
            }, 500) // تأخير التحديث لمدة 500 مللي ثانية
        }
    }

    private fun updateQuantityAndPrice() {
        viewBinding.quantityContent.setText(quantity.toString())
        viewBinding.priceContent.setText(calculatePrice().toString())
    }


    private fun calculatePrice(): Double {
        val basePricePerKilo = when (viewBinding.materialContent.text.toString()) {
            "alumetal" -> 70.0
            "wood" -> 8.0
            "metal" -> 18.0
            "plastic" -> 15.0
            "cardboard" -> 6.0
            "white-glass" -> 14.0
            "brown-glass" -> 14.0
            "paper" -> 15.0
            "battery" -> 40.0
            else -> 10.0 // السعر الافتراضي لكل كيلو
        }

        val quantity = viewBinding.quantityContent.text.toString().toDoubleOrNull() ?: 1.0

        return basePricePerKilo * quantity
    }

    private fun classifyImage(imageUri: Uri) {
        val accessToken = tokenManager.getToken() ?: ""
        val inputStream = contentResolver.openInputStream(imageUri)
        val requestFile = RequestBody.create(MediaType.parse("image/*"), inputStream!!.readBytes())
        val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)

        viewModel.classifyImage(accessToken, body) { isSuccess, message, data ->
            if (isSuccess) {
                // Update material EditText with classification result
                viewBinding.materialContent.setText(data ?: "")
            } else {
                // Handle classification failure
                Toast.makeText(this, "Failed to classify image: $message", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun observeMaterialClassification() {
        // Optionally observe any LiveData related to material classification if required
    }


    private fun setupMaterialSelection() {
        val materials = arrayOf(
            "alumetal",
            "wood",
            "metal",
            "plastic",
            "cardboard",
            "white-glass",
            "brown-glass",
            "paper",
            "battery"
        )

        viewBinding.materialContent.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("اختر نوع المادة")
                .setItems(materials) { dialog, which ->
                    viewBinding.materialContent.setText(materials[which])
                    updateQuantityAndPrice() // تحديث السعر بناءً على الماتريال الجديد
                    dialog.dismiss()
                }
                .show()
        }
    }
}