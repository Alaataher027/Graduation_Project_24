package com.example.graduationproject.api.model.search

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchAddressResponse(

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable

@Parcelize
data class DataItem(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("material")
    val material: String? = null,

    @field:SerializedName("quantity")
    val quantity: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("reject_reason")
    val rejectReason: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("saved")
    val saved: Int? = null,

    @field:SerializedName("location")
    val location: String? = null
) : Parcelable