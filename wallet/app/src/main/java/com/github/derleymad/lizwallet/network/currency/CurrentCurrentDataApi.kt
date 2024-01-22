package com.github.derleymad.lizwallet.network.currency

import com.github.derleymad.lizwallet.model.currency.CurrentCurrencyData
import retrofit2.http.GET
import retrofit2.http.Path


interface CurrentCurrentDataApi {
    @GET("{currency}?localization=false&tickers=true&&sparkline=true")
    suspend fun getCurrentData(@Path(value="currency") currency: String): CurrentCurrencyData
//    https://api.coingecko.com/api/v3/coins
}