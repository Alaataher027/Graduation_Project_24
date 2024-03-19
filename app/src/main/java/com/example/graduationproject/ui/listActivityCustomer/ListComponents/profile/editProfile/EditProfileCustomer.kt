package com.example.graduationproject.ui.listActivityCustomer.ListComponents.profile.editProfile

import android.app.AlertDialog
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityEditProfileCustomerBinding
import com.example.graduationproject.databinding.DialogOptionsBinding
import com.example.graduationproject.ui.ImageProfileViewModel
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.listActivityCustomer.ListComponents.profile.profileView.CustomerProfileViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileCustomer : AppCompatActivity() {
    lateinit var viewBinding: ActivityEditProfileCustomerBinding
    lateinit var viewModel: EditProfileCustomerViewModel
    lateinit var viewModelShow: CustomerProfileViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var imageProfileViewModel: ImageProfileViewModel


//    private val cameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//        if (isGranted) {
//            openCamera()
//        } else {
//            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditProfileCustomerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        tokenManager = TokenManager(this)
        viewModel = EditProfileCustomerViewModel()
        viewModelShow = CustomerProfileViewModel()
        imageProfileViewModel = ImageProfileViewModel()


        fetchUserProfileData()
        updateData()
        onClickBackBtn()
        showDialogOnClickImage()
        showImageProfile()
    }

    private fun uploadImage(imageUri: Uri) {
        val accessToken = tokenManager.getToken() ?: ""
        val inputStream = contentResolver.openInputStream(imageUri)
        val requestFile = RequestBody.create(MediaType.parse("image/*"), inputStream!!.readBytes())
        val body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile)
        imageProfileViewModel.uploadImage(accessToken, body) { isSuccess, message ->
            if (isSuccess) {
                Log.d("EditProfileCustomer", "Image upload successful")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                Log.d("UserProfileData", "Image URL up: ${imageUri}")
//                Log.i("UserProfileData", "body: ${body}")

            } else {
                Log.d("EditProfileCustomer", "Image upload failed")
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val requestImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uploadImage(it) // Pass the obtained Uri directly to the uploadImage function
            }
        }

    private fun openGalleryForImage() {
        requestImageLauncher.launch("image/*")
    }

//    private fun openCamera() {
//        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
//    }

//    private fun checkCameraPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            openCamera()
//        } else {
//            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
//        }
//    }


    //    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            when (requestCode) {
//                PICK_IMAGE_REQUEST -> {
//                    val selectedImageUri: Uri? = data?.data
//                    selectedImageUri?.let {
//                        // Display the selected image in the ImageView
//                        val bitmap =
//                            MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
//                        val circularBitmap = getRoundedBitmap(bitmap)
//                        viewBinding.imageProfile.setImageBitmap(circularBitmap)
//                    }
//                }
//
//                REQUEST_IMAGE_CAPTURE -> {
//                    val imageBitmap = data?.extras?.get("data") as Bitmap?
//                    imageBitmap?.let {
//                        // Display the captured image in the ImageView
//                        viewBinding.imageProfile.setImageBitmap(it)
//                    }
//                }
//            }
//        }
//    }


    // Function to display the image
    private fun displayImage() {
        val imageDrawable = viewBinding.imageProfile.drawable
        if (imageDrawable != null && imageDrawable is BitmapDrawable) {
            val bitmap = imageDrawable.bitmap
            // Display the bitmap in a dialog, ImageView, or any other desired way
            val dialog = AlertDialog.Builder(this)
                .setView(ImageView(this).apply {
                    setImageBitmap(bitmap)
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    setPadding(20, 20, 20, 20)
                })
                .setPositiveButton("OK", null)
                .create()
            dialog.show()
        } else {
            // Handle case where the image has not been set yet
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialogOnClickImage() {
        viewBinding.addImageBtn.setOnClickListener {
            val dialogBinding = DialogOptionsBinding.inflate(layoutInflater)
            val alertDialogBuilder = AlertDialog.Builder(this)
                .setTitle("")
                .setView(dialogBinding.root)

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()

            dialogBinding.option1Button.setOnClickListener {
                Log.d("EditProfileCustomer", "clicked")
                alertDialog.dismiss() // Dismiss the dialog
//                checkCameraPermission()
//                openCamera()

            }

            dialogBinding.option2Button.setOnClickListener {
                alertDialog.dismiss() // Dismiss the dialog
                openGalleryForImage()
            }

            dialogBinding.option3Button.setOnClickListener {
                alertDialog.dismiss() // Dismiss the dialog if needed
                // Handle Option 3 click
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show()
            }


        }
    }


    private fun fetchUserProfileData() {
        val accessToken = TokenManager(this).getToken()
        val userId = tokenManager.getUserId()
        accessToken?.let { token ->
            viewModelShow.viewData(
                accessToken = token,
                userId,
                onDataLoaded = { userData ->
                    userData?.let {
                        Log.d("UserProfileData", "Image URL: ${userData.image}")

                        // Populate EditText fields with user profile data
                        viewBinding.emailCustomer.setText(userData.email ?: "")
                        viewBinding.phoneCustomer.setText(userData.phoneNumber ?: "")
                        viewBinding.governorateCustomer.setText(userData.governorate ?: "")
                        viewBinding.cityCustomer.setText(userData.city ?: "")
                        viewBinding.taxCustomer.setText(userData.tIN ?: "")
                        val requestOptions = RequestOptions().transform(CircleCrop())

                        if (!isDestroyed) { // Check if the activity is destroyed
                            Glide.with(this@EditProfileCustomer)
                                .load(userData.image)
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
        }
    }

    private fun updateData() {
        viewBinding.saveBtn.setOnClickListener {
            val accessToken = tokenManager.getToken() ?: return@setOnClickListener

            // Get the updated values from the EditText fields
            val email = viewBinding.emailCustomer.text.toString().trim()
            val phoneNumber = viewBinding.phoneCustomer.text.toString().trim()
            val governorate = viewBinding.governorateCustomer.text.toString().trim()
            val city = viewBinding.cityCustomer.text.toString().trim()
            val tax = viewBinding.taxCustomer.text.toString().trim()

            // Call the ViewModel functions to update each field individually
            viewModel.updateEmail(
                accessToken,
                email,
                onSuccess = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    finish() // Finish the activity after successful update
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )

            // Call the ViewModel functions to update other fields similarly
            viewModel.updatePhoneNumber(accessToken, phoneNumber, onSuccess = { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            }, onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            })

            viewModel.updateGovernorate(accessToken, governorate, onSuccess = { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            }, onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            })

            viewModel.updateCity(accessToken, city, onSuccess = { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            }, onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            })

            viewModel.updateTAX(accessToken, tax, onSuccess = { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            }, onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            })


        }
    }

    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            // Handle the click event, for example, navigate back one step
            onBackPressed()
        }
    }

    private fun showImageProfile() {
        viewBinding.imageProfile.setOnClickListener {
            displayImage()
        }
    }


//    companion object {
//        private const val PICK_IMAGE_REQUEST = 1
//        private const val REQUEST_IMAGE_CAPTURE = 2
//    }
}