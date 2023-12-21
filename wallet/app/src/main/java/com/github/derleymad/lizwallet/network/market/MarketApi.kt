package com.github.derleymad.lizwallet.network.market
import com.github.derleymad.lizwallet.model.market.MarketData
import retrofit2.Call
import retrofit2.http.GET
interface MarketApi{
    @GET("global-markets")
    suspend fun getMarket(): ArrayList<MarketData>
}