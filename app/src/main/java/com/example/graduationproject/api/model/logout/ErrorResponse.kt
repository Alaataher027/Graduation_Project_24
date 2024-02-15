package com.example.graduationproject.api.model.logout

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorResponse(
    @SerializedName("message")
    val message: String? = null
) : Parcelable
