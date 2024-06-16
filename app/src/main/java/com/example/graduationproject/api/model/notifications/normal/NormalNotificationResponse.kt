package com.example.graduationproject.api.model.notifications.normal

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class NormalNotificationResponse(

	@field:SerializedName("data")
	val data: List<String?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable