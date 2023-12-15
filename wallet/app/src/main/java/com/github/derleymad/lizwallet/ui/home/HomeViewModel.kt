package com.github.derleymad.lizwallet.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.github.derleymad.lizwallet.network.RetrofitInstance
import com.github.derleymad.lizwallet.utils.convertObjectToJson
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class HomeViewModel( val app: Context) : ViewModel() {

    private val _listOfCurrencies= MutableLiveData<ArrayList<ListOfCurrencies>>()
    private val LAST_API_CALL_TIMESTAMP_KEY = "last_api_call_timestamp"

    private fun saveJsonToFile(context: Context, jsonString: String, filename: String) {
        context.openFileOutput(filename, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }
    fun readJsonFromFile(context: Context, filename: String): ArrayList<ListOfCurrencies> {
        val jsonString = context.openFileInput(filename).bufferedReader().use { it.readText() }

        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<ListOfCurrencies>>() {}.type

        return gson.fromJson(jsonString, type)
    }


    fun getCurrencies() {
        val sharedPreferences = app.getSharedPreferences("seu_pref_name",Context.MODE_PRIVATE)
        viewModelScope.launch {
            val lastTimestamp = sharedPreferences.getLong(LAST_API_CALL_TIMESTAMP_KEY, 0)
            if ((System.currentTimeMillis() - lastTimestamp) > TimeUnit.SECONDS.toMillis(30)) {
                RetrofitInstance.api.getListOfCurrencies().enqueue(object : Callback<ArrayList<ListOfCurrencies>> {
                    override fun onResponse(call: Call<ArrayList<ListOfCurrencies>>, response: Response<ArrayList<ListOfCurrencies>>) {
                        if (response.body() != null) {
                            _listOfCurrencies.postValue(response.body())
                            sharedPreferences.edit().putLong(LAST_API_CALL_TIMESTAMP_KEY, System.currentTimeMillis()).apply()
                            saveJsonToFile(app,convertObjectToJson(response.body()!!),"db")
                            Log.i("testing","getFromApi")
                        } else {
                            Log.i("error", "errornull")
                        }
                    }
                    override fun onFailure(call: Call<ArrayList<ListOfCurrencies>>, t: Throwable) {
//                        Log.e("TAGAPI", t.message.toString())
                    }
                })
            } else {
                Log.i("testing","getFromDb")
                _listOfCurrencies.postValue(readJsonFromFile(app,"db"))
            }
        }
    }

    val listOfCurrencies: LiveData<ArrayList<ListOfCurrencies>> = _listOfCurrencies
}