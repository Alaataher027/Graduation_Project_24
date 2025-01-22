package com.example.graduationproject.api.model.notifications.yesAndNo

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ConfirmNotificationSellerResponse(

    @field:SerializedName("data")
    val data: List<DataItemS?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable

@Parcelize
data class DataItemS(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("seller_response")
    val sellerResponse: Int? = null,

    @field:SerializedName("buyer_response")
    val buyerResponse: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("buyer_id")
    val buyerId: String? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("seller_id")
    val sellerId: String? = null
) : Parcelable
