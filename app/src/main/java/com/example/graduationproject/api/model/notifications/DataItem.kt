package com.example.graduationproject.api.model.notifications

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DataItem(

	@field:SerializedName("to_who")
	val toWho: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("from_who")
	val fromWho: String? = null,

	@field:SerializedName("linked_id")
	val linkedId: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable