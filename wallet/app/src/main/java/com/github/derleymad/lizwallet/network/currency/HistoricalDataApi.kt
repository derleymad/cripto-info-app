package com.github.derleymad.lizwallet.network.currency
import com.github.derleymad.lizwallet.model.HistoricalData
import com.github.derleymad.lizwallet.model.currency.CurrentCurrencyData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HistoricalDataApi{
    @GET("{id}/market_chart/range?vs_currency=usd")
    suspend fun getHistoricalData(
        @Path(value="id") id : String,
        @Query("from") from: String,
        @Query("to") to: String
    ): HistoricalData
}