package com.mulosbron.goldmarketcap.service

import retrofit2.http.GET
import retrofit2.Call
import com.mulosbron.goldmarketcap.model.GoldPrice

interface GoldPricesAPI {
    @GET("api/goldprices/latest")
    fun getLatestGoldPrices(): Call<Map<String, GoldPrice>>
}


