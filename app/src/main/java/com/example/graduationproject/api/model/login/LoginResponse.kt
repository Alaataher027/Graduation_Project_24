package com.example.graduationproject.api.model.login

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val loginErrors: LoginErrors? = null
) : Parcelable