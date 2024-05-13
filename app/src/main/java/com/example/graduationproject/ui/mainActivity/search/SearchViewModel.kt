package com.example.graduationproject.ui.mainActivity.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.graduationproject.api.ApiManager
import com.example.graduationproject.api.model.search.DataItem
import com.example.graduationproject.api.model.search.SearchAddressResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val _originalSearchResults = mutableListOf<DataItem?>() // Hold the original unfiltered list
    private val _searchResults = MutableLiveData<List<DataItem?>>()
    val searchResults: LiveData<List<DataItem?>>
        get() = _searchResults

    fun getPostsByPlace(
        accessToken: String,
        address: String
    ) {
        ApiManager.getApisToken(accessToken).searchByAddress(accessToken, address)
            .enqueue(object : Callback<SearchAddressResponse> {
                override fun onResponse(
                    call: Call<SearchAddressResponse>,
                    response: Response<SearchAddressResponse>
                ) {
                    if (response.isSuccessful) {
                        val searchData = response.body()
                        _originalSearchResults.clear() // Clear previous results
                        _originalSearchResults.addAll(searchData?.data ?: emptyList()) // Update with new results
                        _searchResults.value = _originalSearchResults.toList() // Update LiveData
                        val status: Int? = searchData?.status
                        val message: String? = searchData?.message
                        if (status == 200) {
                            // Handle successful response with status 200
                            Log.d("SearchViewModel", "200, $message")
                            searchData?.data?.forEachIndexed { index, dataItem ->
                                Log.d("SearchFragment", "DataItem $index: id=${dataItem?.id}, userId=${dataItem?.userId}, material=${dataItem?.material}, quantity=${dataItem?.quantity}, price=${dataItem?.price}, description=${dataItem?.description}, status=${dataItem?.status}, rejectReason=${dataItem?.rejectReason}, image=${dataItem?.image}, createdAt=${dataItem?.createdAt}, updatedAt=${dataItem?.updatedAt}, saved=${dataItem?.saved}, location=${dataItem?.location}")
                            }

                        } else {
                            // Handle response with status other than 200
                            Log.d("SearchViewModel", "else 200, $message")
                        }
                    } else {
                        // Handle unsuccessful response
                        Log.e("SearchViewModel", "Failed to fetch search posts: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SearchAddressResponse>, t: Throwable) {
                    // Handle failure
                    Log.e("SearchViewModel", "Failed to fetch search posts", t)
                }
            })
    }

    fun filterByMaterial(materials: List<String>) {
        val filteredResults = filterPostsByMaterial(_originalSearchResults, materials)
        _searchResults.value = filteredResults
    }

    private fun filterPostsByMaterial(posts: List<DataItem?>, materials: List<String>): List<DataItem?> {
        return posts.filter { post ->
            val material = post?.material
            materials.any { it.equals(material, ignoreCase = true) }
        }
    }
}

