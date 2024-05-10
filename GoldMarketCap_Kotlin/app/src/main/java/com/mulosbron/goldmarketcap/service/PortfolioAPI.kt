package com.mulosbron.goldmarketcap.service

import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.model.TransactionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body

interface PortfolioAPI {
    @GET("api/portfolio/transactions")
    fun getTransactions(
        @Query("username") username: String
    ): Call<Map<String, List<Transaction>>>

    @POST("api/portfolio/add-transaction")
    fun addTransaction(
        @Query("username") username: String,
        @Query("goldType") goldType: String,
        @Body transaction: Transaction
    ): Call<TransactionResponse>
}
