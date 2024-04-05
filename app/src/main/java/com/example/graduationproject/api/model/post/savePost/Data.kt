package com.example.graduationproject.api.model.post.savePost

import com.google.gson.annotations.SerializedName

data class Data(

    @field:SerializedName("post_id")
    val postId: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)