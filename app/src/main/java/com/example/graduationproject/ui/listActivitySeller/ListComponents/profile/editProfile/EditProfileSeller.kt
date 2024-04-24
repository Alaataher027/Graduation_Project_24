package com.example.graduationproject.ui.listActivitySeller.ListComponents.profile.editProfile

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
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityEditProfileSellerBinding
import com.example.graduationproject.databinding.DialogOptionsBinding
import com.example.graduationproject.ui.ImageProfileViewModel
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.listActivitySeller.ListComponents.profile.profileView.SellerProfileViewModel
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileSeller : AppCompatActivity() {
    private lateinit var viewBinding: ActivityEditProfileSellerBinding
    private lateinit var viewModel: EditProfileSellerViewModel
    lateinit var viewModelShow: SellerProfileViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var imageProfileViewModel: ImageProfileViewModel

    private var glideRequestManager: RequestManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditProfileSellerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        tokenManager = TokenManager(this)
        viewModel = EditProfileSellerViewModel()
        viewModelShow = SellerProfileViewModel()
        imageProfileViewModel = ImageProfileViewModel()

        glideRequestManager = Glide.with(this)

        fetchUserProfileData()
        onClickBackBtn()
        updateData()
        showDialogOnClickImage()
        showImageProfile()


    }

    override fun onDestroy() {
        super.onDestroy()
        // Clear Glide requests when the activity is destroyed
        glideRequestManager?.clearOnStop()
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
                alertDialog.dismiss() // Dismiss the dialog if needed
                // Handle Option 1 click
                Toast.makeText(this, "Take photo", Toast.LENGTH_SHORT).show()
            }

            dialogBinding.option2Button.setOnClickListener {
                alertDialog.dismiss() // Dismiss the dialog if needed
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
                accessToken = token, userId,
                onDataLoaded = { userData ->
                    userData?.let {
                        // Populate EditText fields with user profile data
                        viewBinding.emailSeller.setText(userData.email ?: "")
                        viewBinding.phoneSeller.setText(userData.phoneNumber ?: "")
                        viewBinding.governorateSeller.setText(userData.governorate ?: "")
                        viewBinding.citySeller.setText(userData.city ?: "")
                        viewBinding.streetSeller.setText(userData.street ?: "")
                        viewBinding.quarterSeller.setText(userData.address ?: "")

                        val requestOptions = RequestOptions().transform(CircleCrop())

//                        Glide.with(this)
//                            .load(userData.image)
//                            .apply(requestOptions)
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.error)
//                            .into(viewBinding.imageProfile)

                        if (!isDestroyed) { // Check if the activity is destroyed

                            glideRequestManager?.load(userData.image)
                                ?.apply(requestOptions)
                                ?.placeholder(R.drawable.placeholder) // Placeholder image while loading
                                ?.error(R.drawable.error) // Image to display if loading fails
                                ?.into(viewBinding.imageProfile)
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
            val email = viewBinding.emailSeller.text.toString().trim()
            val phoneNumber = viewBinding.phoneSeller.text.toString().trim()
            val governorate = viewBinding.governorateSeller.text.toString().trim()
            val city = viewBinding.citySeller.text.toString().trim()
            val street = viewBinding.streetSeller.text.toString().trim()
            val address = viewBinding.quarterSeller.text.toString().trim()

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

            viewModel.updateStreet(accessToken, street, onSuccess = { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

            }, onError = { errorMessage ->
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            })

            viewModel.updateResidentialQuarter(
                accessToken,
                address,
                onSuccess = { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                })

            //refreshProfileActivity()
        }
    }


//    private fun updateData() {
//        viewBinding.saveBtn.setOnClickListener {
//            val accessToken = tokenManager.getToken() ?: return@setOnClickListener
//
//            // Get the updated values from the EditText fields
//            val email = viewBinding.emailSeller.text.toString().trim()
//            val phoneNumber = viewBinding.phoneSeller.text.toString().trim()
//            val governorate = viewBinding.governorateSeller.text.toString().trim()
//            val city = viewBinding.citySeller.text.toString().trim()
//            val street = viewBinding.streetSeller.text.toString().trim()
//            val residentialQuarter = viewBinding.quarterSeller.text.toString().trim()
//
//            // Flag to track whether any update fails
//            var hasError = false
//
//            // Function to handle successful update
//            val onSuccess: (String) -> Unit = { message ->
//                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                if (!hasError) {
//                    finish()
//                }
//            }
//
//            // Function to handle error during update
//            val onError: (String) -> Unit = { errorMessage ->
//                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//                hasError = true
//            }
//
//            // Call the ViewModel functions to update each field individually
//            viewModel.updateEmail(accessToken, email, onSuccess, onError)
//            viewModel.updatePhoneNumber(accessToken, phoneNumber, onSuccess, onError)
//            viewModel.updateGovernorate(accessToken, governorate, onSuccess, onError)
//            viewModel.updateCity(accessToken, city, onSuccess, onError)
//            viewModel.updateStreet(accessToken, street, onSuccess, onError)
//            viewModel.updateResidentialQuarter(accessToken, residentialQuarter, onSuccess, onError)
//
//
//        }
//    }


//    private fun updateDate() {
//        viewBinding.saveBtn.setOnClickListener {
//            val accessToken = tokenManager.getToken() ?: return@setOnClickListener
//            val email = viewBinding.emailSeller.text.toString().trim()
//            val phoneNumber = viewBinding.phoneSeller.text.toString().trim()
//            val governorate = viewBinding.governorateSeller.text.toString().trim()
//            val city = viewBinding.citySeller.text.toString().trim()
//            val street = viewBinding.streetSeller.text.toString().trim()
//            val residentialQuarter = viewBinding.quarterSeller.text.toString().trim()
//
//            viewModel.updateProfile(
//                accessToken,
//                email,
//                phoneNumber,
//                governorate,
//                city,
//                street,
//                residentialQuarter,
//                onSuccess = {message->
//                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                    finish() // Finish the activity after successful update
//                },
//                onError = { errorMessage ->
//                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
//                }
//            )
//        }
//    }

    private fun onClickBackBtn() {
        viewBinding.buttonBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun showImageProfile() {
        viewBinding.imageProfile.setOnClickListener {
            displayImage()
        }
    }
}
