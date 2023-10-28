package com.example.graduationproject.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    companion object{         //  عشان يبقوا  static
        private  var retrofit: Retrofit? = null //  ingleton >> عشان مش محتاجة يبقى معايا غير اوبجيكت واحد بس من retrofit
        private fun getInstance():Retrofit{
            if(retrofit==null){
                retrofit=Retrofit.Builder()
                    .baseUrl("https://alshaerawy.aait-sa.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            }
            return retrofit!!
        }
        fun getApis():WebServices{
            return getInstance().create(WebServices::class.java)
        }
    }
}