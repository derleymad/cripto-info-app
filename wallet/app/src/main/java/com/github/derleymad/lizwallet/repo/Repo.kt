package com.github.derleymad.lizwallet.repo

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.derleymad.lizwallet.model.market.MarketData
import com.github.derleymad.lizwallet.network.RetrofitInstance
import com.github.derleymad.lizwallet.utils.convertObjectToJsonMarket
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class Repo(val app: Context) {

    private val LAST_API_CALL_TIMESTAMP_KEY = "last_api_call_timestamp"
    private val LAST_API_NEWS_CALL_TIMESTAMP_KEY = "last_api_call_timestamp"
    private val LAST_API_MARKET_CALL_TIMESTAMP_KEY = "last_api_call_timestamp"
    private val LAST_API_BITCOINTOFIAT_CALL_TIMESTAMP_KEY = "last_api_call_timestamp"

//    suspend fun getMarket(){
//        val sharedPreferences = app.getSharedPreferences("seu_pref_name",Context.MODE_PRIVATE)
//            val lastTimestamp = sharedPreferences.getLong(LAST_API_MARKET_CALL_TIMESTAMP_KEY, 0)
//            if ((System.currentTimeMillis() - lastTimestamp) > TimeUnit.SECONDS.toMillis(100)) {
//                RetrofitInstance.apiMarket.getMarket().enqueue(object :
//                    Callback<ArrayList<MarketData>> {
//                    override fun onResponse(call: Call<ArrayList<MarketData>>, response: Response<ArrayList<MarketData>>) {
//                        if (response.body() != null) {
//                            _market.postValue(formatMarket(response.body()!!))
//
//                            sharedPreferences.edit().putLong(LAST_API_MARKET_CALL_TIMESTAMP_KEY, System.currentTimeMillis()).apply()
//                            saveJsonToFile(app, convertObjectToJsonMarket(formatMarket(response.body()!!)),"db_market")
//                        } else {
//                            Log.i("error", "errornull")
//                        }
//                    }
//                    override fun onFailure(call: Call<ArrayList<MarketData>>, t: Throwable) {
//                        _market.postValue(readJsonFromMarket(app,"db_market"))
//                    }
//                })
//            } else {
//                _market.postValue(readJsonFromMarket(app,"db_market"))
//            }
//    }
//
//




}