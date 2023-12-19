package com.github.derleymad.lizwallet.ui.home

import BitcoinToFiat
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.github.derleymad.lizwallet.model.market.MarketData
import com.github.derleymad.lizwallet.model.market.MarketToRecyclerData
import com.github.derleymad.lizwallet.model.news.RawNews
import com.github.derleymad.lizwallet.network.RetrofitInstance
import com.github.derleymad.lizwallet.utils.convertObjectToJson
import com.github.derleymad.lizwallet.utils.convertObjectToJsonBitcoinToFiatBrl
import com.github.derleymad.lizwallet.utils.convertObjectToJsonMarket
import com.github.derleymad.lizwallet.utils.convertObjectToJsonNews
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.horizontalsystems.bitcoincore.BitcoinCore
import io.horizontalsystems.bitcoincore.models.BalanceInfo
import io.horizontalsystems.bitcoincore.models.TransactionInfo
import io.horizontalsystems.bitcoinkit.BitcoinKit
import io.horizontalsystems.hdwalletkit.HDExtendedKey
import io.horizontalsystems.hdwalletkit.HDWallet
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class HomeViewModel( val app: Context) : ViewModel() {

    private val _listOfCurrencies= MutableLiveData<ArrayList<ListOfCurrencies>>()
    private val _newsRaw = MutableLiveData<RawNews>()
    private val _market = MutableLiveData<ArrayList<MarketToRecyclerData>>()
    val balance = MutableLiveData<Long?>()
    val transactionInfo = MutableLiveData<List<TransactionInfo>>()
    val synced = MutableLiveData(false)
    val syncing = MutableLiveData("")
    val stateBitcore = MutableLiveData<BitcoinCore.KitState>()
    val isLoading = MutableLiveData(false)
    val fiatBrl = MutableLiveData<BitcoinToFiat>()
    val watchOnlyAdress = MutableLiveData("")

    private val LAST_API_CALL_TIMESTAMP_KEY = "last_api_call_timestamp"
    private val LAST_API_NEWS_CALL_TIMESTAMP_KEY = "last_api_call_timestamp"
    private val LAST_API_MARKET_CALL_TIMESTAMP_KEY = "last_api_call_timestamp"
    private val LAST_API_BITCOINTOFIAT_CALL_TIMESTAMP_KEY = "last_api_call_timestamp"

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

    fun readJsonFromFileNews(context: Context, filename: String) : RawNews{
        val jsonString = context.openFileInput(filename).bufferedReader().use { it.readText() }
        val gson = Gson()
        val type: Type = object : TypeToken<RawNews>() {}.type
        return gson.fromJson(jsonString, type)
    }
    fun readJsonFromMarket(context: Context, filename: String) : ArrayList<MarketToRecyclerData>{
        val jsonString = context.openFileInput(filename).bufferedReader().use { it.readText() }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<MarketToRecyclerData>>() {}.type
        return gson.fromJson(jsonString, type)
    }
    fun readJsonFromFiatConverter(context: Context, filename: String) : BitcoinToFiat{
        val jsonString = context.openFileInput(filename).bufferedReader().use { it.readText() }
        val gson = Gson()
        val type: Type = object : TypeToken<BitcoinToFiat>() {}.type
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

    fun getMarket(){
        val sharedPreferences = app.getSharedPreferences("seu_pref_name",Context.MODE_PRIVATE)
        viewModelScope.launch {
            val lastTimestamp = sharedPreferences.getLong(LAST_API_MARKET_CALL_TIMESTAMP_KEY, 0)
            if ((System.currentTimeMillis() - lastTimestamp) > TimeUnit.SECONDS.toMillis(100)) {
                RetrofitInstance.apiMarket.getMarket().enqueue(object : Callback<ArrayList<MarketData>> {
                    override fun onResponse(call: Call<ArrayList<MarketData>>, response: Response<ArrayList<MarketData>>) {
                        if (response.body() != null) {
                            _market.postValue(formatMarket(response.body()!!))

                            sharedPreferences.edit().putLong(LAST_API_MARKET_CALL_TIMESTAMP_KEY, System.currentTimeMillis()).apply()
                            saveJsonToFile(app, convertObjectToJsonMarket(formatMarket(response.body()!!)),"db_market")
                        } else {
                            Log.i("error", "errornull")
                        }
                    }
                    override fun onFailure(call: Call<ArrayList<MarketData>>, t: Throwable) {
                    }
                })
            } else {
                _market.postValue(readJsonFromMarket(app,"db_market"))
            }
        }
    }
    fun getBitcoinToFiatConverter(){
        val sharedPreferences = app.getSharedPreferences("seu_pref_name",Context.MODE_PRIVATE)
        viewModelScope.launch {
            val lastTimestamp = sharedPreferences.getLong(LAST_API_BITCOINTOFIAT_CALL_TIMESTAMP_KEY, 0)
            if ((System.currentTimeMillis() - lastTimestamp) > TimeUnit.SECONDS.toMillis(100)) {
                RetrofitInstance.apiConverter.getMarket().enqueue(object : Callback<BitcoinToFiat> {
                    override fun onResponse(call: Call<BitcoinToFiat>, response: Response<BitcoinToFiat>) {
                        if (response.body() != null) {
//                            _market.postValue(formatMarket(response.body()!!))
                            fiatBrl.postValue(response.body()!!)
                            sharedPreferences.edit().putLong(LAST_API_BITCOINTOFIAT_CALL_TIMESTAMP_KEY, System.currentTimeMillis()).apply()
                            saveJsonToFile(app, convertObjectToJsonBitcoinToFiatBrl(response.body()!!),"db_fiat_brl")
                        } else {
                            Log.i("error", "errornull")
                        }
                    }
                    override fun onFailure(call: Call<BitcoinToFiat>, t: Throwable) {
                    }
                })
            } else {
                fiatBrl.postValue(readJsonFromFiatConverter(app,"db_fiat_brl"))
            }
        }
    }

    fun getNews(){
        val sharedPreferences = app.getSharedPreferences("seu_pref_name",Context.MODE_PRIVATE)
        viewModelScope.launch {
            val lastTimestamp = sharedPreferences.getLong(LAST_API_NEWS_CALL_TIMESTAMP_KEY, 0)
            if ((System.currentTimeMillis() - lastTimestamp) > TimeUnit.SECONDS.toMillis(100)) {
                RetrofitInstance.apiNews.getRawNews().enqueue(object : Callback<RawNews> {
                    override fun onResponse(call: Call<RawNews>, response: Response<RawNews>) {
                        if (response.body() != null) {
                            _newsRaw.postValue(response.body())
                            sharedPreferences.edit().putLong(LAST_API_NEWS_CALL_TIMESTAMP_KEY, System.currentTimeMillis()).apply()
                            saveJsonToFile(app, convertObjectToJsonNews(response.body()!!),"db_news")
                        } else {
                            Log.i("error", "errornull")
                        }
                    }
                    override fun onFailure(call: Call<RawNews>, t: Throwable) {
                    }
                })
            } else {
                _newsRaw.postValue(readJsonFromFileNews(app,"db_news"))
            }
        }
    }

    fun initBitoinKit(walletName : String ?= ""){
        isLoading.postValue(true)
        val sharedPreferences =
            app?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val xpub = sharedPreferences?.getString("xpub", null)

        if (xpub!= null) {
//            Toast.makeText(this, "XPub recuperado: $savedXpub", Toast.LENGTH_SHORT).show()
            val context = app
            val extendedKey = HDExtendedKey(xpub)
            val bitcoinKit = BitcoinKit(
                context = context,
                extendedKey = extendedKey,
                walletId = "$walletName",
                syncMode = BitcoinCore.SyncMode.Api(),
                networkType = BitcoinKit.NetworkType.MainNet,
                confirmationsThreshold = 6,
                purpose = HDWallet.Purpose.BIP84
            )
            bitcoinKit.listener = object : BitcoinKit.Listener{
                override fun onBalanceUpdate(balance: BalanceInfo) {
                    this@HomeViewModel.balance.postValue(bitcoinKit.balance.spendable)
                    super.onBalanceUpdate(balance)
                }

                override fun onKitStateUpdate(state: BitcoinCore.KitState) {
                    if(state == BitcoinCore.KitState.Synced){
                        Log.i("bitcoin testing","synced")
                        synced.postValue(true)
                        isLoading.postValue(false)
                        val disposables = CompositeDisposable()
                        bitcoinKit.transactions().subscribe { transactionInfos ->
                            this@HomeViewModel.transactionInfo.postValue(transactionInfos)
                            Log.i("transactioninfo",transactionInfos.toString())
                        }.let {
                            disposables.add(it)
                        }
                    }
                    else{
                        Log.i("bitcoin testing sync",state.toString())
                        stateBitcore.postValue(state)
                    }
                    super.onKitStateUpdate(state)
                }
            }

            balance.postValue(bitcoinKit.balance.spendable)
            bitcoinKit.start()
        }

    }

    val listOfCurrencies: LiveData<ArrayList<ListOfCurrencies>> = _listOfCurrencies
    val newsRaw : LiveData<RawNews> = _newsRaw
    val market: LiveData<ArrayList<MarketToRecyclerData>> = _market
}