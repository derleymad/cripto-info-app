package com.github.derleymad.lizwallet.model

import kotlinx.serialization.Serializable

data class ListOfCurrencies(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    val current_price: Double,
    val market_cap: Double,
    val market_cap_rank: Int,
    val fully_diluted_valuation: Double,
    val total_volume: Double,
    val high_24h: Double,
    val low_24h: Double,
    val price_change_24h: Double,
    val price_change_percentage_24h: Double,
    val market_cap_change_24h: Double,
    val market_cap_change_percentage_24h: Double,
    val circulating_supply: Double,
    val total_supply: Double,
    val max_supply: Double,
    val ath: Double,
    val atl_change_percentage: Double,
    val atl_date: String,
    val last_updated: String,
    val price_change_percentage_14d_in_currency: Double,
    val price_change_percentage_1y_in_currency: Double,
    val price_change_percentage_200d_in_currency: Double,
    val price_change_percentage_24h_in_currency: Double,
    val price_change_percentage_30d_in_currency: Double,
    val price_change_percentage_7d_in_currency: Double

)
