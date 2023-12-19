package com.github.derleymad.lizwallet.model.market

data class MarketToRecyclerData(
    val name: String,
    val value : Double,
    val percentage : Double,
    val marketData: ArrayList<Double>
)
