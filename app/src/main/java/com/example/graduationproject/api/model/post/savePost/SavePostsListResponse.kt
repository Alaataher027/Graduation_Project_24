package com.example.graduationproject.api.model.post.savePost

import com.google.gson.annotations.SerializedName

data class SavePostsListResponse(

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
)