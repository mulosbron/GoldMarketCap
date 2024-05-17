package com.mulosbron.goldmarketcap.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface QueryAPI {
    @GET("api/queries/search/{query}")
    fun searchGoldPriceNames(@Path("query") query: String): Call<List<String>>
}