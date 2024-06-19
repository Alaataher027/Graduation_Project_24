//package com.example.graduationproject.ui.mainActivity.fragment.createPost
//
//import android.app.AlertDialog
//import android.net.Uri
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.Spinner
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import com.bumptech.glide.Glide
//import com.example.graduationproject.R
//import com.example.graduationproject.databinding.ActivityCreatPostBinding
//import com.example.graduationproject.ui.login.TokenManager
//import com.example.graduationproject.ui.mainActivity.fragment.home.UserDataHomeViewModel
//import okhttp3.MediaType
//import okhttp3.MultipartBody
//import okhttp3.RequestBody
//
//class CreatPostActivity : AppCompatActivity() {
//
//    private lateinit var viewBinding: ActivityCreatPostBinding
//    private lateinit var viewModel: CreatePostViewModel
//    private lateinit var tokenManager: TokenManager
//    private lateinit var viewModelProfile: UserDataHomeViewModel
//    private var quantityValue = 1
////    private lateinit var spinner: Spinner
//
//    // Material options to display in the dialog
//    private val materialOptions = arrayOf(
//        "alumetal",
//        "wood",
//        "metal",
//        "plastic",
//        "cardboard",
//        "white-glass",
//        "brown-glass",
//        "paper",
//        "battery"
//    )
//
//    // Price per unit for each material type
//    private val materialPrices = mapOf(
//        "alumetal" to 70,
//        "wood" to 8,
//        "metal" to 18,
//        "plastic" to 15,
//        "cardboard" to 6,
//        "white-glass" to 14,
//        "brown-glass" to 14,
//        "paper" to 15,
//        "battery" to 40
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        viewBinding = ActivityCreatPostBinding.inflate(layoutInflater)
//        setContentView(viewBinding.root)
//
//        tokenManager = TokenManager(this)
//
//        val factory = CreatePostViewModelFactory(tokenManager)
//        viewModel = ViewModelProvider(this, factory).get(CreatePostViewModel::class.java)
//
//        viewModelProfile = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)
//
//        // Disable EditText and Spinner initially
//        viewBinding.materialContent.isEnabled = false
//        // Make EditText non-editable
//        viewBinding.materialContent.isFocusable = false
//        viewBinding.materialContent.isFocusableInTouchMode = false
//
//        onClickBack()
//
//        viewBinding.addImage.setOnClickListener {
//            openGalleryForImage()
//        }
//
//        viewUserData()
//        shareBtnView()
//
//        viewBinding.decBtn.setOnClickListener {
//            decreaseQuantity()
//        }
//
//        viewBinding.incBtn.setOnClickListener {
//            increaseQuantity()
//        }
//
////        spinner = findViewById(R.id.quantity_spinner)
//
////        ArrayAdapter.createFromResource(
////            this,
////            R.array.quantity_options,
////            android.R.layout.simple_spinner_item
////        ).also { adapter ->
////            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
////            spinner.adapter = adapter
////        }
//
////        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
////            override fun onItemSelected(
////                parent: AdapterView<*>,
////                view: View?,
////                position: Int,
////                id: Long
////            ) {
////                // Handle the selection here
////                val selectedOption = parent.getItemAtPosition(position).toString()
////                // Save the selected quantity type based on the selected option
////            }
////
////            override fun onNothingSelected(parent: AdapterView<*>) {
////                // Another interface callback
////            }
////        }
//
//        // Set click listener for materialContent EditText
//        viewBinding.materialContent.setOnClickListener {
//            showMaterialOptionsDialog()
//        }
//
//        // Add TextWatcher to quantityContent EditText
//        // Add TextWatcher to quantityContent EditText
//        viewBinding.quantityContent.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                // Not needed
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                // Not needed
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                val quantityText = s?.toString()
//                if (!quantityText.isNullOrEmpty()) {
//                    try {
//                        quantityValue = quantityText.toInt()
//                        updatePrice()
//                    } catch (e: NumberFormatException) {
//                        quantityValue = 1 // Set default quantity if input is invalid
//                        viewBinding.quantityContent.setText(quantityValue.toString())
//                        updatePrice()
//                    }
//                } else {
//                    quantityValue = 1 // Set default quantity if empty
//                    updatePrice()
//                }
//            }
//        })
//
//    }
//
//    private fun decreaseQuantity() {
//        if (quantityValue > 1) {
//            quantityValue--
//            viewBinding.quantityContent.setText(quantityValue.toString())
//        } else {
//            Toast.makeText(this, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show()
//        }
//        updatePrice()
//    }
//
//    private fun increaseQuantity() {
//        quantityValue++
//        viewBinding.quantityContent.setText(quantityValue.toString())
//        updatePrice()
//    }
//
//    private fun updatePrice() {
//        val selectedMaterial = viewBinding.materialContent.text.toString()
//        val pricePerUnit = materialPrices[selectedMaterial] ?: 0
//        val totalPrice = quantityValue * pricePerUnit
//        viewBinding.priceContent.setText(totalPrice.toString())
//    }
//
//
//    private fun viewUserData() {
//        val accessToken = tokenManager.getToken() ?: ""
//        val userId = tokenManager.getUserId()
//        viewModelProfile.getData(accessToken, userId, { userData ->
//            viewBinding.userName.text = userData?.name ?: ""
//
//            if (!isDestroyed) {
//                userData?.let { user ->
//                    Glide.with(this@CreatPostActivity)
//                        .load(user.image)
//                        .placeholder(R.drawable.placeholder)
//                        .error(R.drawable.error)
//                        .into(viewBinding.userImage)
//                }
//            }
//        }, { errorMessage ->
//            Log.e("UserDataError", errorMessage)
//        })
//    }
//
//    private val requestImageLauncher =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//            uri?.let {
//                setupViews(it)
//            }
//        }
//
//    private fun openGalleryForImage() {
//        requestImageLauncher.launch("image/*")
//    }
//
//    private fun setupViews(imageUri: Uri?) {
//        // Disable EditText and Spinner when a new image is selected
//        viewBinding.materialContent.isEnabled = false
//
//        val accessToken = tokenManager.getToken() ?: ""
//
//        imageUri?.let {
//            viewBinding.addImage.setImageURI(it)
//            val inputStream = contentResolver.openInputStream(imageUri)
//
//            if (inputStream != null) {
//                val requestFile =
//                    RequestBody.create(MediaType.parse("image/*"), inputStream.readBytes())
//                val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)
//                viewModel.classifyImage(accessToken, body) { isSuccess, message, material ->
//                    if (isSuccess) {
//                        val editableMaterial = Editable.Factory.getInstance().newEditable(material)
//                        viewBinding.materialContent.setText(editableMaterial)
//
//                        // Enable EditText and Spinner after classification
//                        viewBinding.materialContent.isEnabled = true
//                        updatePrice() // Update price when material is classified
//                    } else {
//                        Toast.makeText(
//                            this,
//                            "Failed to classify image: $message",
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                        // Enable EditText and Spinner after classification failure
//                        viewBinding.materialContent.isEnabled = true
//                    }
//                }
//
//                viewBinding.shareBtn.setOnClickListener {
//                    val descriptionText = viewBinding.descriptionContent.text.toString()
//                    val quantityText = viewBinding.quantityContent.text.toString()
//
//                    // Check if description or quantity is empty
//                    if (descriptionText.isEmpty()) {
//                        Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT)
//                            .show()
//                        return@setOnClickListener
//                    }
//
//                    if (quantityText.isEmpty() || quantityText.toIntOrNull() == null) {
//                        Toast.makeText(
//                            this,
//                            "Quantity cannot be empty or invalid",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        return@setOnClickListener
//                    }
//
//                    val description = RequestBody.create(
//                        MediaType.parse("text/plain"),
//                        descriptionText
//                    )
//
//                    val selectedQuantityShowed = "Kilo"
//                    val quantityValue =
//                        quantityText.toIntOrNull() ?: 1 // Ensure a valid integer value
//
//                    val quantity = RequestBody.create(
//                        MediaType.parse("text/plain"),
//                        "$quantityValue $selectedQuantityShowed"
//                    )
//
//                    val pricePerUnit =
//                        calculatePricePerUnit(viewBinding.materialContent.text.toString())
//                    val totalPrice = calculateTotalPrice(quantityValue, pricePerUnit)
//
//                    // Set the calculated total price in the price_content EditText
//                    viewBinding.priceContent.setText(totalPrice.toString())
//
//                    // Get the current material from the EditText and create a RequestBody for it
//                    val material = RequestBody.create(
//                        MediaType.parse("text/plain"),
//                        viewBinding.materialContent.text.toString()
//                    )
//
//                    // Create the price RequestBody
//                    val price =
//                        RequestBody.create(MediaType.parse("text/plain"), totalPrice.toString())
//
//                    viewModel.createPost(accessToken, description, quantity, price, material, body)
//                }
//            } else {
//                Toast.makeText(this, "Failed to read image file", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//
//    private fun shareBtnView() {
//        val accessToken = tokenManager.getToken() ?: ""
//        val userId = tokenManager.getUserId()
//
//        viewModel.postCreationResult.observe(this, { result ->
//            if (result.isSuccess) {
//                val postId = result.getOrNull()?.data?.id
//                Toast.makeText(
//                    this,
//                    "Post created successfully with ID: $postId",
//                    Toast.LENGTH_SHORT
//                ).show()
//                finish()
//            } else {
//                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
//                Toast.makeText(this, "Failed to create post: $errorMessage", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
//
//        viewModel.error.observe(this, { errorMessage ->
//            Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
//        })
//    }
//
//    private fun onClickBack() {
//        viewBinding.buttonBack.setOnClickListener {
//            finish()
//        }
//    }
//
//    private fun showMaterialOptionsDialog() {
//        val builder = AlertDialog.Builder(this)
//        builder.setTitle("Select Material")
//            .setItems(materialOptions) { dialog, which ->
//                val selectedMaterial = materialOptions[which]
//                viewBinding.materialContent.setText(selectedMaterial)
//                updatePrice() // Update price when material is selected
//                dialog.dismiss()
//            }
//        builder.create().show()
//    }
//
//    // Function to calculate the price per unit
//    private fun calculatePricePerUnit(material: String): Int {
//        return materialPrices[material] ?: 0
//    }
//
//    // Function to calculate the total price
//    private fun calculateTotalPrice(quantity: Int, pricePerUnit: Int): Int {
//        return quantity * pricePerUnit
//    }
//
//}

package com.example.graduationproject.ui.mainActivity.fragment.createPost

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityCreatPostBinding
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
    private var quantityValue = 1

    // Material options to display in the dialog
    private val materialOptions = arrayOf(
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

    // Price per unit for each material type
    private val materialPrices = mapOf(
        "alumetal" to 70,
        "wood" to 8,
        "metal" to 18,
        "plastic" to 15,
        "cardboard" to 6,
        "white-glass" to 14,
        "brown-glass" to 14,
        "paper" to 15,
        "battery" to 40
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreatPostBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        tokenManager = TokenManager(this)

        val factory = CreatePostViewModelFactory(tokenManager)
        viewModel = ViewModelProvider(this, factory).get(CreatePostViewModel::class.java)

        viewModelProfile = ViewModelProvider(this).get(UserDataHomeViewModel::class.java)

        // Observe the postCreationResult
        viewModel.postCreationResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show()
                finish() // Finish the activity
            }.onFailure {
                Toast.makeText(this, "Failed to create post: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }


        // Disable EditText and Spinner initially
        viewBinding.materialContent.isEnabled = false
        // Make EditText non-editable
        viewBinding.materialContent.isFocusable = false
        viewBinding.materialContent.isFocusableInTouchMode = false

        onClickBack()

        viewBinding.addImage.setOnClickListener {
            openGalleryForImage()
        }

        viewUserData()
//        shareBtnView()

        viewBinding.decBtn.setOnClickListener {
            decreaseQuantity()
        }

        viewBinding.incBtn.setOnClickListener {
            increaseQuantity()
        }

        // Set click listener for materialContent EditText
        viewBinding.materialContent.setOnClickListener {
            showMaterialOptionsDialog { selectedMaterial ->
                viewBinding.materialContent.setText(selectedMaterial)
                updatePrice()
            }
        }

        // Add TextWatcher to quantityContent EditText
        viewBinding.quantityContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed
            }

            override fun afterTextChanged(s: Editable?) {
                val quantityText = s?.toString()
                if (!quantityText.isNullOrEmpty()) {
                    try {
                        quantityValue = quantityText.toInt()
                        updatePrice()
                    } catch (e: NumberFormatException) {
                        quantityValue = 1 // Set default quantity if input is invalid
                        viewBinding.quantityContent.setText(quantityValue.toString())
                        updatePrice()
                    }
                } else {
                    quantityValue = 1 // Set default quantity if empty
                    updatePrice()
                }
            }
        })
    }

    private fun decreaseQuantity() {
        if (quantityValue > 1) {
            quantityValue--
            viewBinding.quantityContent.setText(quantityValue.toString())
        } else {
            Toast.makeText(this, "Quantity cannot be less than 1", Toast.LENGTH_SHORT).show()
        }
        updatePrice()
    }

    private fun increaseQuantity() {
        quantityValue++
        viewBinding.quantityContent.setText(quantityValue.toString())
        updatePrice()
    }

    private fun updatePrice() {
        val selectedMaterial = viewBinding.materialContent.text.toString()
        val pricePerUnit = materialPrices[selectedMaterial] ?: 0
        val totalPrice = quantityValue * pricePerUnit
        viewBinding.priceContent.setText(totalPrice.toString())
    }

    private fun viewUserData() {
        val accessToken = tokenManager.getToken() ?: ""
        val userId = tokenManager.getUserId()
        viewModelProfile.getData(accessToken, userId, { userData ->
            viewBinding.userName.text = userData?.name ?: ""

            if (!isDestroyed) {
                userData?.let { user ->
                    Glide.with(this@CreatPostActivity)
                        .load(user.image)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.error)
                        .into(viewBinding.userImage)
                }
            }
        }, { errorMessage ->
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
        // Disable EditText and Spinner when a new image is selected
        viewBinding.materialContent.isEnabled = false

        val accessToken = tokenManager.getToken() ?: ""

        imageUri?.let {
            viewBinding.addImage.setImageURI(it)
            val inputStream = contentResolver.openInputStream(imageUri)

            if (inputStream != null) {
                val requestFile =
                    RequestBody.create(MediaType.parse("image/*"), inputStream.readBytes())
                val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)
                viewModel.classifyImage(accessToken, body) { isSuccess, message, material ->
                    if (isSuccess) {
                        val editableMaterial = Editable.Factory.getInstance().newEditable(material)
                        viewBinding.materialContent.setText(editableMaterial)

                        // Show dialog_classification after 2 seconds
                        Handler(Looper.getMainLooper()).postDelayed({
                            material?.let { it1 -> showClassificationDialog(it1) }
                        }, 2000)

                        // Enable EditText and Spinner after classification
                        viewBinding.materialContent.isEnabled = true
                        updatePrice() // Update price when material is classified
                    } else {
                        Toast.makeText(
                            this,
                            "Failed to classify image: $message",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Enable EditText and Spinner after classification failure
                        viewBinding.materialContent.isEnabled = true
                    }
                }

                viewBinding.shareBtn.setOnClickListener {
                    val descriptionText = viewBinding.descriptionContent.text.toString()
                    val quantityText = viewBinding.quantityContent.text.toString()

                    // Check if description or quantity is empty
                    if (descriptionText.isEmpty()) {
                        Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnClickListener
                    }

                    if (quantityText.isEmpty() || quantityText.toIntOrNull() == null) {
                        Toast.makeText(
                            this,
                            "Quantity cannot be empty or invalid",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    val description = RequestBody.create(
                        MediaType.parse("text/plain"),
                        descriptionText
                    )

                    val selectedQuantityShowed = "Kilo"
                    val quantityValue =
                        quantityText.toIntOrNull() ?: 1 // Ensure a valid integer value

                    val quantity = RequestBody.create(
                        MediaType.parse("text/plain"),
                        "$quantityValue $selectedQuantityShowed"
                    )

                    val pricePerUnit =
                        calculatePricePerUnit(viewBinding.materialContent.text.toString())
                    val totalPrice = calculateTotalPrice(quantityValue, pricePerUnit)

                    // Set the calculated total price in the price_content EditText
                    viewBinding.priceContent.setText(totalPrice.toString())

                    // Get the current material from the EditText and create a RequestBody for it
                    val material = RequestBody.create(
                        MediaType.parse("text/plain"),
                        viewBinding.materialContent.text.toString()
                    )

                    // Create the price RequestBody
                    val price =
                        RequestBody.create(MediaType.parse("text/plain"), totalPrice.toString())

                    viewModel.createPost(accessToken, description, quantity, price, material, body)

                }
            } else {
                Toast.makeText(this, "Failed to read image file", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showClassificationDialog(initialMaterial: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_classification, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        val materialTextView = dialogView.findViewById<TextView>(R.id.material)
        val dropListImageView = dialogView.findViewById<ImageView>(R.id.drop_list)
        val saveButton = dialogView.findViewById<Button>(R.id.save_btn)

        materialTextView.text = initialMaterial

        dropListImageView.setOnClickListener {
            showMaterialOptionsDialog {
                materialTextView.text = it
            }
        }

        saveButton.setOnClickListener {
            viewBinding.materialContent.setText(materialTextView.text.toString())
            updatePrice()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showMaterialOptionsDialog(onMaterialSelected: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Material")
            .setItems(materialOptions) { dialog, which ->
                val selectedMaterial = materialOptions[which]
                onMaterialSelected(selectedMaterial)
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun calculatePricePerUnit(material: String): Int {
        return materialPrices[material] ?: 0
    }

    private fun calculateTotalPrice(quantity: Int, pricePerUnit: Int): Int {
        return quantity * pricePerUnit
    }

    private fun shareBtnView() {
        val descriptionText = viewBinding.descriptionContent.text.toString()
        val quantityText = viewBinding.quantityContent.text.toString()

//        // Check if description or quantity is empty
//        if (descriptionText.isEmpty()) {
//            Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT).show()
//            return@setOnClickListener
//        }
//
//        if (quantityText.isEmpty() || quantityText.toIntOrNull() == null) {
//            Toast.makeText(this, "Quantity cannot be empty or invalid", Toast.LENGTH_SHORT).show()
//            return@setOnClickListener
//        }

        val description = RequestBody.create(
            MediaType.parse("text/plain"),
            descriptionText
        )

        val selectedQuantityShowed = "Kilo"
        val quantityValue = quantityText.toIntOrNull() ?: 1 // Ensure a valid integer value

        val quantity = RequestBody.create(
            MediaType.parse("text/plain"),
            "$quantityValue $selectedQuantityShowed"
        )

        val pricePerUnit = calculatePricePerUnit(viewBinding.materialContent.text.toString())
        val totalPrice = calculateTotalPrice(quantityValue, pricePerUnit)

        // Set the calculated total price in the price_content EditText
        viewBinding.priceContent.setText(totalPrice.toString())

        // Get the current material from the EditText and create a RequestBody for it
        val material = RequestBody.create(
            MediaType.parse("text/plain"),
            viewBinding.materialContent.text.toString()
        )

        // Create the price RequestBody
        val price = RequestBody.create(MediaType.parse("text/plain"), totalPrice.toString())

        val imageUri = viewBinding.addImage.tag as? Uri
        if (imageUri != null) {
            val inputStream = contentResolver.openInputStream(imageUri)
            if (inputStream != null) {
                val requestFile =
                    RequestBody.create(MediaType.parse("image/*"), inputStream.readBytes())
                val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)
                viewModel.createPost(
                    tokenManager.getToken() ?: "",
                    description,
                    quantity,
                    price,
                    material,
                    body
                )
            } else {
                Toast.makeText(this, "Failed to read image file", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }

    }

    private fun onClickBack() {
        viewBinding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }
}
