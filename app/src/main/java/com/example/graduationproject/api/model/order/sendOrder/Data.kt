package com.example.graduationproject.api.model.order.sendOrder

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class Data(

	@field:SerializedName("post_id")
	val postId: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("buyer_id")
	val buyerId: String? = null,

	@field:SerializedName("seller_id")
	val sellerId: Int? = null
) : Parcelable