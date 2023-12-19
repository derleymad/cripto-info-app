package com.github.derleymad.lizwallet.utils

import BitcoinToFiat
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.github.derleymad.lizwallet.model.market.MarketData
import com.github.derleymad.lizwallet.model.market.MarketToRecyclerData
import com.github.derleymad.lizwallet.model.news.RawNews
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

fun convertObjectToJson(listOfCurrencies: ArrayList<ListOfCurrencies>): String {
    val gson = Gson()
    val type: Type = object : TypeToken<ArrayList<ListOfCurrencies>>() {}.type
    return gson.toJson(listOfCurrencies, type)
}

fun convertObjectToJsonNews(rawNews: RawNews):String{
    val gson = Gson()
    val type : Type = object : TypeToken<RawNews>() {}.type
    return gson.toJson(rawNews,type)
}
fun convertObjectToJsonMarket(market : ArrayList<MarketToRecyclerData>):String{
    val gson = Gson()
    val type : Type = object : TypeToken<ArrayList<MarketToRecyclerData>>() {}.type
    return gson.toJson(market,type)
}
fun convertObjectToJsonBitcoinToFiatBrl(fiatBrl: BitcoinToFiat):String{
    val gson = Gson()
    val type : Type = object : TypeToken<BitcoinToFiat>() {}.type
    return gson.toJson(fiatBrl,type)
}
