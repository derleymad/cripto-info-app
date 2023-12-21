package com.github.derleymad.lizwallet.repo

import BitcoinToFiat
import android.content.Context
import android.util.Log
import com.github.derleymad.lizwallet.database.Dao
import com.github.derleymad.lizwallet.database.Dao.formatMarket
import com.github.derleymad.lizwallet.database.Dao.readJsonFromFiatConverter
import com.github.derleymad.lizwallet.database.Dao.readJsonFromFile
import com.github.derleymad.lizwallet.database.Dao.readJsonFromFileNews
import com.github.derleymad.lizwallet.database.Dao.readJsonFromMarket
import com.github.derleymad.lizwallet.database.Dao.saveJsonToFile
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.github.derleymad.lizwallet.model.market.MarketToRecyclerData
import com.github.derleymad.lizwallet.model.news.RawNews
import com.github.derleymad.lizwallet.network.RetrofitInstance
import java.util.concurrent.TimeUnit

class Repo(val app: Context, val retrofit : RetrofitInstance) {

    private val LAST_CURRENCIES_TIMESTAMP = "last_api_call_timestamp"
    private val LAST_NEWS_TIMESTAMP = "last_api_call_timestamp"
    private val LAST_MARKET_TIMESTAMP = "last_api_call_timestamp"
    private val LAST_BRL_PRICE_TIMESTAMP = "last_api_call_timestamp"
    val sharedPreferences = app.getSharedPreferences("repository", Context.MODE_PRIVATE)

    suspend fun getCurrencies(): ArrayList<ListOfCurrencies>?{
        val lastTimestamp = sharedPreferences.getLong(LAST_CURRENCIES_TIMESTAMP, 0)
        val currentTimeMillis = System.currentTimeMillis()

        if ((currentTimeMillis - lastTimestamp) > TimeUnit.SECONDS.toMillis(30)) {
            try {
                val body = retrofit.api.getListOfCurrencies()
                sharedPreferences.edit().putLong(LAST_CURRENCIES_TIMESTAMP, currentTimeMillis).apply()
                saveJsonToFile(app, Dao.convertObjectToJsonCurrencies(body), "db_currencies")
                Log.i("repo currencies","from api")
                return body
            } catch (e: Exception) {
                Log.i("NETWORK", "NetworkConnection $e")
            }
        }

        val cachedData = readJsonFromFile(app, "db_currencies")
        if (cachedData != null) {
            // If there is cached data and it's within the 10-second interval, return it
//            if ((currentTimeMillis - lastTimestamp) <= TimeUnit.SECONDS.toMillis(10)) {
                Log.i("repo currencies", "from db")
                return cachedData
//            }
        }

        return null // If no data in API or cache, return null
    }

    suspend fun getMarket() : ArrayList<MarketToRecyclerData>?{
//        val sharedPreferences = app.getSharedPreferences("repository", Context.MODE_PRIVATE)
        val lastTimestamp = sharedPreferences.getLong(LAST_MARKET_TIMESTAMP, 0)

        if ((System.currentTimeMillis() - lastTimestamp) > TimeUnit.SECONDS.toMillis(30)) {
            try {
                val body = retrofit.apiMarket.getMarket()
                sharedPreferences.edit().putLong(LAST_MARKET_TIMESTAMP, System.currentTimeMillis()).apply()
                saveJsonToFile(app, Dao.convertObjectToJsonMarket(formatMarket(body)),"db_market")
                Log.i("repo market","from api")
                return formatMarket(body)
            } catch (e: Exception) {
                Log.i("NETWORK","NetworkConection $e")
            }
        }

        val cachedData = readJsonFromMarket(app,"db_market")
        if (cachedData!=null) {
            Log.i("repo market","from db")
            return cachedData
        }

        return null // se não houver dados na API ou em cache, retorna uma lista vazia
    }
    suspend fun getNews(): RawNews? {
        val lastTimestamp = sharedPreferences.getLong(LAST_NEWS_TIMESTAMP, 0)
        val currentTimeMillis = System.currentTimeMillis()

        if ((currentTimeMillis - lastTimestamp) > TimeUnit.SECONDS.toMillis(30)) {
            try {
                val body = retrofit.apiNews.getRawNews()
                sharedPreferences.edit().putLong(LAST_NEWS_TIMESTAMP, currentTimeMillis).apply()
                saveJsonToFile(app, Dao.convertObjectToJsonNews(body), "db_news")
                Log.i("repo news", "from api")
                return body
            } catch (e: Exception) {
                Log.i("repo NETWORK2", "NetworkConnection $e")
            }
        }

        try {
            val cachedData = readJsonFromFileNews(app, "db_news")
            if (cachedData != null) {
             Log.i("repo news", "from db")
             return cachedData
            }
        } catch (e: Exception) {
            Log.i("repo DATABASE", "DatabaseConnection $e")
        }

        return null // If no data in API or cache, return null
    }

    suspend fun getBrlPrice(): BitcoinToFiat? {
        val lastTimestamp = sharedPreferences.getLong(LAST_BRL_PRICE_TIMESTAMP, 0)
        val currentTimeMillis = System.currentTimeMillis()

        if ((currentTimeMillis - lastTimestamp) > TimeUnit.SECONDS.toMillis(30)) {
            try {
                val body = retrofit.apiConverter.getBrlPrice()
                sharedPreferences.edit().putLong(LAST_BRL_PRICE_TIMESTAMP, currentTimeMillis).apply()
                saveJsonToFile(app, Dao.convertObjectToJsonBitcoinToFiatBrl(body), "db_brl")
                Log.i("repo currencies", "from api")
                return body
            } catch (e: Exception) {
                Log.i("NETWORK", "NetworkConnection $e")
            }
        }

        val cachedData = readJsonFromFiatConverter(app, "db_brl")
        if (cachedData != null) {
          Log.i("repo brlPrice", "from db")
          return cachedData
        }

        return null // If no data in API or cache, return null
    }


}