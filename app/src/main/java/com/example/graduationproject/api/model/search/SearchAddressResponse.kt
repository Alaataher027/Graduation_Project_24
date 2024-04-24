package com.example.graduationproject.api.model.search

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class SearchAddressResponse(

    @field:SerializedName("data")
    val data: List<List<DataItem?>?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable

@Parcelize
data class DataItem(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("quantity")
    val quantity: String? = null,

    @field:SerializedName("material")
    val material: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("reject_reason")
    val rejectReason: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("saved")
    val saved: Int? = 0,

	@field:SerializedName("location")
	val location: String? = null


) : Parcelable