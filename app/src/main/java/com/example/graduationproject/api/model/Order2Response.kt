package com.example.graduationproject.api.model

import com.google.gson.annotations.SerializedName

data class Order2Response(

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)

data class DataItem(

    @field:SerializedName("post_id")
    val postId: Int? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("online_payment_method")
    val onlinePaymentMethod: Any? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("buyer_id")
    val buyerId: String? = null,

    @field:SerializedName("seller_id")
    val sellerId: String? = null,

    @field:SerializedName("payment_method")
    val paymentMethod: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)