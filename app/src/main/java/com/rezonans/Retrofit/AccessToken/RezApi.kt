package com.rezonans.Retrofit.AccessToken

import com.rezonans.Retrofit.AccessToken.Data
import retrofit2.Call
import retrofit2.http.POST

interface RezApi {

    @POST("refresh")
    fun getData(): Call<Data>

}