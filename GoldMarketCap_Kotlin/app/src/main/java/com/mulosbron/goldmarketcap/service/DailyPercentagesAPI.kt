package com.mulosbron.goldmarketcap.service

import com.mulosbron.goldmarketcap.model.DailyPercentage
import io.reactivex.Observable
import retrofit2.http.GET

interface DailyPercentagesAPI {
    @GET("api/daily-percentages/latest")
    fun getLatestDailyPercentages(): Observable<Map<String, DailyPercentage>>
}