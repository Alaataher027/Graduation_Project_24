package com.example.graduationproject.api.model.post

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.example.graduationproject.api.model.post.Data
import com.google.gson.annotations.SerializedName

@Parcelize
data class PostResponse(

    @field:SerializedName("data")
	val data: Data? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: Int? = null
) : Parcelable