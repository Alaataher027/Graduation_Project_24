package com.example.graduationproject.api.model.order.sendOrder

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class OrderResponse(

    @field:SerializedName("data")
	val data: Data? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Int? = null
) : Parcelable