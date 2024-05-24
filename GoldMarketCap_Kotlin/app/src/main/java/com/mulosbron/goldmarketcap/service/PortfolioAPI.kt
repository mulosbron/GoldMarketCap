package com.mulosbron.goldmarketcap.service

import com.mulosbron.goldmarketcap.model.Transaction
import com.mulosbron.goldmarketcap.model.TransactionResponse
import com.mulosbron.goldmarketcap.model.TransactionUpdateModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

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

    @DELETE("api/portfolio/delete-goldtype")
    fun deleteGoldType(
        @Query("username") username: String,
        @Query("goldType") goldType: String
    ): Call<Void>

    @DELETE("api/portfolio/delete-transaction")
    fun deleteTransaction(
        @Query("username") username: String,
        @Query("goldType") goldType: String,
        @Query("transactionId") transactionId: String
    ): Call<Void>

    @GET("api/portfolio/get-transaction")
    fun getTransaction(
        @Query("username") username: String,
        @Query("transactionId") transactionId: String
    ): Call<Transaction>

    @PATCH("api/portfolio/update-transaction")
    fun updateTransaction(
        @Query("username") username: String,
        @Query("transactionId") transactionId: String,
        @Body updateModel: TransactionUpdateModel
    ): Call<Void>
}
