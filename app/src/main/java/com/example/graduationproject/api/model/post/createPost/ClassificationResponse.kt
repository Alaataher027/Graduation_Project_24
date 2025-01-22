package com.example.graduationproject.api.model.post.createPost

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ClassificationResponse(

	@field:SerializedName("path")
	val path: String? = null,

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("test")
	val test: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
) : Parcelable
