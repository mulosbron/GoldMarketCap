package com.mulosbron.goldmarketcap.model

data class DailyGoldPrice(
    val name: String,
    val buyingPrice: Int,
    val sellingPrice: Int
)

data class DailyPercentage(
    val name: String,
    val buyingPrice: Double,
    val sellingPrice: Double
)
