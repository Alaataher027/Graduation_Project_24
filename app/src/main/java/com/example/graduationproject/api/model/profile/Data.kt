package com.example.graduationproject.api.model.profile

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Data(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("residential_quarter")
	val residentialQuarter: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("verificationToken")
	val verificationToken: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: String? = null,

	@field:SerializedName("social_id")
	val socialId: String? = null,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("social_type")
	val socialType: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("governorate")
	val governorate: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("organization")
	val organization: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("TIN")
	val tIN: Int? = null,

	@field:SerializedName("phone_number")
	val phoneNumber: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable