package com.mulosbron.goldmarketcap.service

import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.model.TransactionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface PortfolioAPI {
    @POST("api/portfolio/add-transaction")
    fun addTransaction(
        @Query("username") username: String,
        @Query("goldType") goldType: String,
        @Body transaction: Transaction
    ): Call<TransactionResponse>
}
