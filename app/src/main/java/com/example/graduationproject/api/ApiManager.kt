package com.example.graduationproject.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    companion object {
        private var retrofit: Retrofit? = null

        // Singleton instance of Retrofit for API calls without token
        fun getInstance(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://alshaerawy.aait-sa.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

        fun getLocationInstance(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("https://atfawry.fawrystaging.com/ECommerceWeb/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

//        fun getInstanceWithoutBase(): Retrofit {
//            if (retrofit == null) {
//                retrofit = Retrofit.Builder()
//                    .baseUrl("https://rekiatestapi.pythonanywhere.com/")// placeholder
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//            }
//            return retrofit!!
//        }
//
//
//
//        fun getApiWithoutBase(): WebServices {
//            return getInstanceWithoutBase().create(WebServices::class.java)
//        }

        // Method for API calls that require a token
        fun getApisToken(accessToken: String): WebServices {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .method(original.method(), original.body())
                val request = requestBuilder.build()
                chain.proceed(request)
            }

            val client = httpClient.build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://alshaerawy.aait-sa.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(WebServices::class.java)
        }

        // Method to get API service for calls without token
        fun getApis(): WebServices {
            return getInstance().create(WebServices::class.java)
        }

        fun getLocationApis(): WebServices {
            return getLocationInstance().create(WebServices::class.java)
        }
    }
}