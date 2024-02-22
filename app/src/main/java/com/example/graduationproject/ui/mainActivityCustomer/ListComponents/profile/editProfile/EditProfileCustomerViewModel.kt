package com.example.graduationproject.ui.mainActivityCustomer.ListComponents.profile.editProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.editProfile.EditProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileCustomerViewModel : ViewModel() {

    fun updateEmail(
        accessToken: String,
        email: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("EditCustomerViewModel", "Updating email...")

        ApiManager.getApisToken(accessToken).editEmail(accessToken, email)
            .enqueue(object : Callback<EditProfileResponse> {
                override fun onResponse(
                    call: Call<EditProfileResponse>,
                    response: Response<EditProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message

                        if (status == 200) {
                            onSuccess(message ?: "Email updated successfully")
                        } else {
                            onError(message ?: "Unknown error")
                        }
                    } else {
                        val errorMessage = "Error ${response.code()}: ${response.message()}"
                        onError(errorMessage)
                    }
                }

                override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                    val errorMessage = t.message ?: "Unknown error"
                    onError(errorMessage)
                }
            })
    }

    fun updatePhoneNumber(
        accessToken: String,
        phoneNumber: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("EditCustomerViewModel", "Updating phone number...")

        ApiManager.getApisToken(accessToken).editPhone(accessToken, phoneNumber)
            .enqueue(object : Callback<EditProfileResponse> {
                override fun onResponse(
                    call: Call<EditProfileResponse>,
                    response: Response<EditProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message

                        if (status == 200) {
                            onSuccess(message ?: "Phone number updated successfully")
                        } else {
                            onError(message ?: "Unknown error")
                        }
                    } else {
                        val errorMessage = "Error ${response.code()}: ${response.message()}"
                        onError(errorMessage)
                    }
                }

                override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                    val errorMessage = t.message ?: "Unknown error"
                    onError(errorMessage)
                }
            })
    }


    fun updateGovernorate(
        accessToken: String,
        governorate: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("EditCustomerViewModel", "Updating governorate...")

        ApiManager.getApisToken(accessToken).editGovernorate(accessToken, governorate)
            .enqueue(object : Callback<EditProfileResponse> {
                override fun onResponse(
                    call: Call<EditProfileResponse>,
                    response: Response<EditProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message

                        if (status == 200) {
                            onSuccess(message ?: "Governorate updated successfully")
                        } else {
                            onError(message ?: "Unknown error")
                        }
                    } else {
                        val errorMessage = "Error ${response.code()}: ${response.message()}"
                        onError(errorMessage)
                    }
                }

                override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                    val errorMessage = t.message ?: "Unknown error"
                    onError(errorMessage)
                }
            })
    }


    fun updateCity(
        accessToken: String,
        city: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("EditCustomerViewModel", "Updating city...")

        ApiManager.getApisToken(accessToken).editCity(accessToken, city)
            .enqueue(object : Callback<EditProfileResponse> {
                override fun onResponse(
                    call: Call<EditProfileResponse>,
                    response: Response<EditProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message

                        Log.d("EditCustomerProfiel", "first :${response.body()?.data?.city}")

                        if (status == 200) {
                            Log.d("EditCustomerProfiel", "success :${response.body()?.data?.city}")

                            onSuccess(message ?: "city updated successfully")
                        } else {
                            Log.d("EditCustomerProfiel", "not 200 :${response.body()?.data?.city}")

                            onError(message ?: "Unknown error")
                        }
                    } else {
                        Log.d("EditCustomerProfiel", "error :${response.body()?.data?.city}")

                        val errorMessage = "Error ${response.code()}: ${response.message()}"
                        onError(errorMessage)
                    }
                }

                override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                    val errorMessage = t.message ?: "Unknown error"
                    onError(errorMessage)
                }
            })
    }

    fun updateTAX(
        accessToken: String,
        tax: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("EditCustomerViewModel", "Updating TAX...")

        ApiManager.getApisToken(accessToken).editTAXNumber(accessToken, tax)
            .enqueue(object : Callback<EditProfileResponse> {
                override fun onResponse(
                    call: Call<EditProfileResponse>,
                    response: Response<EditProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message


                        Log.d("EditCustomerProfiel", "success :${response.body()?.data?.tIN}")
                        if (status == 200) {
                            Log.d("EditCustomerProfiel", "success :${response.body()?.data?.tIN}")

                            onSuccess(message ?: "tax updated successfully")
                        } else {
                            Log.d("EditCustomerProfiel", "not 200: ${response.body()?.data?.tIN}")
                            onError(message ?: "Unknown error")
                        }
                    } else {
                        Log.d("EditCustomerProfiel", " error: ${response.body()?.data?.tIN}")
                        val errorMessage = "Error ${response.code()}: ${response.message()}"
                        onError(errorMessage)
                    }
                }

                override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                    val errorMessage = t.message ?: "Unknown error"
                    onError(errorMessage)
                }
            })
    }
}