package com.github.derleymad.lizwallet.database

import BitcoinToFiat
import android.content.Context
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.github.derleymad.lizwallet.model.market.MarketData
import com.github.derleymad.lizwallet.model.market.MarketToRecyclerData
import com.github.derleymad.lizwallet.model.news.RawNews
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.FileNotFoundException
import java.lang.reflect.Type

object Dao {
    fun formatMarket(list: ArrayList<MarketData>) : ArrayList<MarketToRecyclerData>{
        val recyclerMarketList = arrayListOf<MarketToRecyclerData>()

        val listValorDeMercado = arrayListOf<Double>()
        val listVolume = arrayListOf<Double>()
        val listMercadoDefi= arrayListOf<Double>()
        val listTVL = arrayListOf<Double>()

        for(i in list){
            listValorDeMercado.add(i.market_cap)
            listVolume.add(i.volume)
            listMercadoDefi.add(i.defi_market_cap)
            listTVL.add(i.tvl)
        }

        recyclerMarketList.add(
            MarketToRecyclerData(
                name = "Valor de mercado",
                value = list[0].market_cap,
                percentage = ((list[list.size - 1].market_cap - list[0].market_cap) / list[0].market_cap) * 100,
                marketData = listValorDeMercado
            )
        )

        recyclerMarketList.add(
            MarketToRecyclerData(
                name = "Volume em 24h",
                value = list[0].volume,
                percentage = ((list[0].volume - list[list.size - 1].volume) / list[list.size - 1].volume) * 100,
                marketData = listVolume
            )
        )

        recyclerMarketList.add(
            MarketToRecyclerData(
                name = "Mercado de DeFi",
                value = list[0].defi_market_cap,
                percentage = ((list[list.size - 1].defi_market_cap - list[0].defi_market_cap) / list[0].defi_market_cap) * 100,
                marketData = listMercadoDefi
            )
        )

        recyclerMarketList.add(
            MarketToRecyclerData(
                name = "TVL no DeFi",
                value = list[0].tvl,
                percentage = ((list[0].tvl - list[list.size - 1].tvl) / list[list.size - 1].tvl) * 100,
                marketData = listTVL
            )
        )


        return recyclerMarketList

    }

    fun saveJsonToFile(context: Context, jsonString: String, filename: String) {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }

    fun readJsonFromFile(context: Context, filename: String): ArrayList<ListOfCurrencies>? {
        try {
            val jsonString = context.openFileInput(filename).bufferedReader().use { it.readText() }
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<ListOfCurrencies>>() {}.type
            return gson.fromJson(jsonString, type)
        }catch (e: FileNotFoundException){
            return null
        }
    }

    fun readJsonFromFileNews(context: Context, filename: String) : RawNews? {
        try {
            val jsonString = context.openFileInput(filename).bufferedReader().use { it.readText() }
            val gson = Gson()
            val type: Type = object : TypeToken<RawNews>() {}.type
            return gson.fromJson(jsonString, type)

        }catch (e: FileNotFoundException){
            return null
        }
    }
    fun readJsonFromMarket(context: Context, filename: String) : ArrayList<MarketToRecyclerData>?{
        try {
            val jsonString = context.openFileInput(filename).bufferedReader().use { it.readText() }
            val gson = Gson()
            val type: Type = object : TypeToken<ArrayList<MarketToRecyclerData>>() {}.type
            return gson.fromJson(jsonString, type)
        }catch (e: FileNotFoundException){
            return null
        }
    }
    fun readJsonFromFiatConverter(context: Context, filename: String) : BitcoinToFiat?{
        try {
            val jsonString = context.openFileInput(filename).bufferedReader().use { it.readText() }
            val gson = Gson()
            val type: Type = object : TypeToken<BitcoinToFiat>() {}.type
            return gson.fromJson(jsonString, type)
        }catch (e: FileNotFoundException){
            return null
        }

    }



    fun convertObjectToJsonCurrencies(listOfCurrencies: ArrayList<ListOfCurrencies>): String {
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



}