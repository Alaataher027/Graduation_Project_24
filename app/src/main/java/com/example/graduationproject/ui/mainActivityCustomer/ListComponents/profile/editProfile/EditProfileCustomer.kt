package com.example.graduationproject.ui.mainActivityCustomer.ListComponents.profile.editProfile

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.graduationproject.databinding.ActivityEditProfileCustomerBinding
import com.example.graduationproject.databinding.DialogOptionsBinding
import com.example.graduationproject.ui.login.TokenManager
import com.example.graduationproject.ui.mainActivityCustomer.ListComponents.profile.profileView.CustomerProfileViewModel

class EditProfileCustomer : AppCompatActivity() {
    lateinit var viewBinding: ActivityEditProfileCustomerBinding
    lateinit var viewModel: EditProfileCustomerViewModel
    lateinit var viewModelShow: CustomerProfileViewModel
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditProfileCustomerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        tokenManager = TokenManager(this)
        viewModel = EditProfileCustomerViewModel()
        viewModelShow = CustomerProfileViewModel()

        fetchUserProfileData()
        updateData()
        onClickBackBtn()
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
                        viewBinding.emailCustomer.setText(userData.email ?: "")
                        viewBinding.phoneCustomer.setText(userData.phoneNumber ?: "")
                        viewBinding.governorateCustomer.setText(userData.governorate ?: "")
                        viewBinding.cityCustomer.setText(userData.city ?: "")
                        viewBinding.taxCustomer.setText(userData.tIN?: "")

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
}