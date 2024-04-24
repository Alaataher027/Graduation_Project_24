package com.example.graduationproject.ui.listActivitySeller.ListComponents.profile.editProfile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.editProfile.EditProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProfileSellerViewModel : ViewModel() {

    fun updateEmail(
        accessToken: String,
        email: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("EditSellerViewModel", "Updating email...")

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
        Log.d("EditSellerViewModel", "Updating phone number...")

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
        Log.d("EditSellerViewModel", "Updating governorate...")

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
        Log.d("EditSellerViewModel", "Updating city...")

        ApiManager.getApisToken(accessToken).editCity(accessToken, city)
            .enqueue(object : Callback<EditProfileResponse> {
                override fun onResponse(
                    call: Call<EditProfileResponse>,
                    response: Response<EditProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message

                        if (status == 200) {
                            onSuccess(message ?: "city updated successfully")
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

    fun updateStreet(
        accessToken: String,
        street: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("EditSellerViewModel", "Updating street...")

        ApiManager.getApisToken(accessToken).editSellerStreet(accessToken, street)
            .enqueue(object : Callback<EditProfileResponse> {
                override fun onResponse(
                    call: Call<EditProfileResponse>,
                    response: Response<EditProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message

                        if (status == 200) {
                            onSuccess(message ?: "city updated successfully")
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

    fun updateResidentialQuarter(
        accessToken: String,
        address: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("EditSellerViewModel", "Updating address...")

        ApiManager.getApisToken(accessToken).editSellerQuarter(accessToken, address)
            .enqueue(object : Callback<EditProfileResponse> {
                override fun onResponse(
                    call: Call<EditProfileResponse>,
                    response: Response<EditProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val status: Int? = response.body()?.status
                        val message: String? = response.body()?.message

                        if (status == 200) {
                            onSuccess(message ?: "city updated successfully")
                        } else {
                            onError(message ?: "Unknown error")
                        }
                    } else {
                        Log.d("EditSellerProfile",  " ${accessToken},cite:  ${response.body()?.data?.city}")
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


//    fun updateProfile(
//        accessToken: String,
//        email: String?,
//        phoneNumber: String?,
//        governorate: String?,
//        city: String?,
//        street: String?,
//        residentialQuarter: String?,
//        onSuccess: (String) -> Unit,
//        onError: (String) -> Unit
//    ) {
//        Log.d("EditSellerViewModel", "Updating profile...")
//
//        ApiManager.getApisToken(accessToken).editSellerProfile(
//            accessToken,
//            email,
//            phoneNumber,
//            governorate,
//            city,
//            street,
//            residentialQuarter
//        ).enqueue(object : Callback<EditProfileResponse> {
//            override fun onResponse(
//                call: Call<EditProfileResponse>,
//                response: Response<EditProfileResponse>
//            ) {
//
//                if (response.isSuccessful) {
//                    Log.d("EditSellerViewModel", "Profile updated successfully")
//
//                    val status: Int? = response.body()?.status
//                    val message: String? = response.body()?.message
//
//                    if (status == 200) {
//                        Log.d("EditSellerViewModel", "Status 200: $message")
//                        onSuccess(message ?: "Profile updated successfully")
//                    } else {
//                        Log.e("EditSellerViewModel", "Status not 200: $message")
//                        onError(message ?: "Unknown error")
//                    }
//                } else {
//                    val errorMessage = "Error ${response.code()}: ${response.message()}"
//                    Log.e("EditSellerViewModel", errorMessage)
//                    onError(errorMessage)
//                }
//            }
//
//            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
//                val errorMessage = t.message ?: "Unknown error"
//                Log.e("EditSellerViewModel", errorMessage)
//                onError(errorMessage)
//            }
//        })
//    }
}