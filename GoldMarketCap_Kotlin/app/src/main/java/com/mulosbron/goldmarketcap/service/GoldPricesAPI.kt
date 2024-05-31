package com.mulosbron.goldmarketcap.service

import com.mulosbron.goldmarketcap.model.DailyGoldPrice
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GoldPricesAPI {
    @GET("api/gold-prices/latest")
    fun getLatestGoldPrices(): Observable<Map<String, DailyGoldPrice>>

    @GET("api/gold-prices/latest/{product}")
    fun getGoldPrice(@Path("product") productName: String): Call<DailyGoldPrice>
}


