package com.example.graduationproject.api.model.login

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LoginErrors(

	@field:SerializedName("password")
	val password: List<String?>? = null,

	@field:SerializedName("email")
	val email: List<String?>? = null
) : Parcelable