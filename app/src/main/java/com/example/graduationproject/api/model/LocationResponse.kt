package com.example.graduationproject.api.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class LocationResponse(

	@field:SerializedName("LocationResponse")
	val locationResponse: List<LocationResponseItem?>? = null
) : Parcelable

@Parcelize
data class AreaDataModelsItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("streetDataModels")
	val streetDataModels: String? = null,

	@field:SerializedName("namePrimaryLang")
	val namePrimaryLang: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("nameSecondaryLang")
	val nameSecondaryLang: String? = null
) : Parcelable

@Parcelize
data class CityDataModelsItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("areaDataModels")
	val areaDataModels: List<AreaDataModelsItem?>? = null,

	@field:SerializedName("namePrimaryLang")
	val namePrimaryLang: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("nameSecondaryLang")
	val nameSecondaryLang: String? = null
) : Parcelable

@Parcelize
data class LocationResponseItem(

	@field:SerializedName("cityDataModels")
	val cityDataModels: List<CityDataModelsItem?>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("namePrimaryLang")
	val namePrimaryLang: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("nameSecondaryLang")
	val nameSecondaryLang: String? = null
) : Parcelable

@Parcelize
data class StreetDataModelsItem(

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("namePrimaryLang")
	val namePrimaryLang: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("nameSecondaryLang")
	val nameSecondaryLang: String? = null
) : Parcelable
