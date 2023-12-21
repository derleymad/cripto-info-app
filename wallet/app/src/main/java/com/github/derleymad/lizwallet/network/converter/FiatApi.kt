package com.github.derleymad.lizwallet.network.converter
import BitcoinToFiat
import com.github.derleymad.lizwallet.model.market.MarketData
import retrofit2.Call
import retrofit2.http.GET

interface FiatApi{
    @GET("price?ids=bitcoin&vs_currencies=brl")
    suspend fun getBrlPrice(): BitcoinToFiat
}