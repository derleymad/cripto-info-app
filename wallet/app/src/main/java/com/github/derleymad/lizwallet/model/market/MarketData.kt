package com.github.derleymad.lizwallet.model.market

data class MarketData (

    val date : Int=1,
    val market_cap : Double=1.0,
    val defi_market_cap : Double=1.0,
    val volume : Double=1.0,
    val btc_dominance : Double=1.0,
    val tvl : Double=1.0
)