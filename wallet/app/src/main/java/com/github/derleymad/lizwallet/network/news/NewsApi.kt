package com.github.derleymad.lizwallet.network.news
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.github.derleymad.lizwallet.model.news.RawNews
import retrofit2.Call
import retrofit2.http.GET

interface NewsApi{
    @GET("?feeds=cointelegraph,theblock,decrypt&extraParams=Blocksdecoded&excludeCategories=Sponsored")
    fun getRawNews(): Call<RawNews>
}