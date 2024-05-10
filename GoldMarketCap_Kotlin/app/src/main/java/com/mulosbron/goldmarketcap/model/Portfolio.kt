package com.mulosbron.goldmarketcap.model

data class Transaction(
    val id: String,
    val date: String,
    val transactionType: String,
    val amount: Int,
    val price: Int
)

data class TransactionResponse(
    val message: String
)

