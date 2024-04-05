package com.example.graduationproject.api.model.post.savePost

import com.google.gson.annotations.SerializedName

data class SavePostResponse(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)