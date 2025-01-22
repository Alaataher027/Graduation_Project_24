package com.example.graduationproject.ui.listActivitySeller.ListComponents.profile.editProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.LocationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel : ViewModel() {

    private val _locationData = MutableLiveData<List<String>>()
    val locationData: LiveData<List<String>> get() = _locationData

    fun fetchLocationData() {
        ApiManager.getLocationApis().getLocationData().enqueue(object : Callback<LocationResponse> {
            override fun onResponse(
                call: Call<LocationResponse>,
                response: Response<LocationResponse>
            ) {
                if (response.isSuccessful) {
                    val locationResponse = response.body()
                    locationResponse?.let { data ->
                        // Extract names from AreaDataModelsItem
                        val areaNames = data.locationResponse?.flatMap { it?.cityDataModels!! }
                            ?.flatMap { it?.areaDataModels!! }
                            ?.mapNotNull { it?.name }

                        // Update LiveData with the list of names
                        _locationData.value = areaNames!!
                    }
                } else {
                    // Handle unsuccessful response
                    // You can emit an error state here if needed
                }
            }

            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                // Handle failure
                // You can emit an error state here if needed
            }
        })
    }
}
