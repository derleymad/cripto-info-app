package com.github.derleymad.lizwallet.network

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
}