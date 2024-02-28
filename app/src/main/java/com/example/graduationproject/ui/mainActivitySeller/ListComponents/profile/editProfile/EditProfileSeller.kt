package com.example.graduationproject.ui.mainActivitySeller.ListComponents.profile.editProfile

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityEditProfileSellerBinding
import com.example.graduationproject.databinding.ActivitySellerProfileBinding
import com.example.graduationproject.databinding.DialogOptionsBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivitySeller.ListComponents.profile.profileView.SellerProfileViewModel

class EditProfileSeller : AppCompatActivity() {
    private lateinit var viewBinding: ActivityEditProfileSellerBinding
    private lateinit var viewModel: EditProfileSellerViewModel
    lateinit var viewModelShow: SellerProfileViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditProfileSellerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        tokenManager = TokenManager(this)
        viewModel = EditProfileSellerViewModel()
        viewModelShow = SellerProfileViewModel()

        fetchUserProfileData()
        onClickBackBtn()
        updateData()
        showDialogOnClickImage()

    }

    private fun showDialogOnClickImage() {
        viewBinding.imageProfile.setOnClickListener {
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
                // Handle Option 2 click
                Toast.makeText(this, "Add from Gallery", Toast.LENGTH_SHORT).show()
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
        accessToken?.let { token ->
            viewModelShow.viewData(
                accessToken = token,
                onDataLoaded = { userData ->
                    userData?.let {
                        // Populate EditText fields with user profile data
                        viewBinding.emailSeller.setText(userData.email ?: "")
                        viewBinding.phoneSeller.setText(userData.phoneNumber ?: "")
                        viewBinding.governorateSeller.setText(userData.governorate ?: "")
                        viewBinding.citySeller.setText(userData.city ?: "")
                        viewBinding.streetSeller.setText(userData.street ?: "")
                        viewBinding.quarterSeller.setText(userData.residentialQuarter ?: "")
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
            val residentialQuarter = viewBinding.quarterSeller.text.toString().trim()

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
                residentialQuarter,
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
}
