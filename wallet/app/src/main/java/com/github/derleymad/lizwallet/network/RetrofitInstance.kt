package com.github.derleymad.lizwallet.network

import androidx.core.graphics.createBitmap
import com.github.derleymad.lizwallet.network.converter.FiatApi
import com.github.derleymad.lizwallet.network.currency.CurrentCurrentDataApi
import com.github.derleymad.lizwallet.network.currency.HistoricalDataApi
import com.github.derleymad.lizwallet.network.market.MarketApi
import com.github.derleymad.lizwallet.network.news.NewsApi
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: CurrenciesApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/coins/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrenciesApi::class.java)
    }

    val apiNews : NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://min-api.cryptocompare.com/data/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    val apiMarket : MarketApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.blocksdecoded.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarketApi::class.java)
    }
    val apiConverter : FiatApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/simple/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FiatApi::class.java)
    }
    val apiCurrentCurrencyHistoricalData : HistoricalDataApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/coins/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HistoricalDataApi::class.java)
    }
    val apiCurrentCurrencyData : CurrentCurrentDataApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.coingecko.com/api/v3/coins/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrentCurrentDataApi::class.java)
    }


}