package com.example.graduationproject.api.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class RegisterResponse(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("errors")
    val errors: Errors? = null

) : Parcelable

@Parcelize
data class Errors(

    @field:SerializedName("password")
    val password: List<String>? = null,

    @field:SerializedName("name")
    val name: List<String>? = null,

    @field:SerializedName("phone_number")
    val phoneNumber: List<String>? = null,

    @field:SerializedName("email")
    val email: List<String>? = null

) : Parcelable
