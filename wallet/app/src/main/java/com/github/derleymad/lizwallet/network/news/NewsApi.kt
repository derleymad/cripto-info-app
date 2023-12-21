package com.github.derleymad.lizwallet.network.news
import com.github.derleymad.lizwallet.model.news.RawNews
import retrofit2.http.GET
interface NewsApi{
    @GET("news/?feeds=cointelegraph,theblock,decrypt&extraParams=Blocksdecoded&excludeCategories=Sponsored")
    suspend fun getRawNews(): RawNews
}