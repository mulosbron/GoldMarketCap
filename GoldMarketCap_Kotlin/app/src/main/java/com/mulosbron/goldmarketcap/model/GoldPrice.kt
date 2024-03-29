package com.mulosbron.goldmarketcap.model

import com.google.gson.annotations.SerializedName

data class GoldPrice(
    @SerializedName("name")
    val name: String,

    @SerializedName("alisFiyati")
    val buyingPrice: Double,

    @SerializedName("satisFiyati")
    val sellingPrice: Double
)



