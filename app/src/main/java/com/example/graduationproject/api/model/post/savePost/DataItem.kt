package com.example.graduationproject.api.model.post.savePost

import com.google.gson.annotations.SerializedName

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

    @field:SerializedName("saved")
    val saved: Int? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("reject_reason")
    val rejectReason: Any? = null,

    @field:SerializedName("status")
    val status: String? = null
)