package com.mulosbron.goldmarketcap.model

data class Transaction(
    val id: String,
    val date: String,
    val transactionType: String,
    val amount: Double,
    val price: Double
)

data class TransactionResponse(
    val message: String
)

