package com.mulosbron.goldmarketcap.service

import retrofit2.http.GET
import retrofit2.Call
import com.mulosbron.goldmarketcap.model.DailyPercentage

interface DailyPercentagesAPI {
    @GET("api/daily-percentages/latest")
    fun getLatestDailyPercentages(): Call<Map<String, DailyPercentage>>
}