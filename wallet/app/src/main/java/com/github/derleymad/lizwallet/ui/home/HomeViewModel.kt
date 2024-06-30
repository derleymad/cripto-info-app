package com.github.derleymad.lizwallet.ui.home

import BitcoinToFiat
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.derleymad.lizwallet.model.HistoricalData
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.github.derleymad.lizwallet.model.currency.CurrentCurrencyData
import com.github.derleymad.lizwallet.model.market.MarketToRecyclerData
import com.github.derleymad.lizwallet.model.news.RawNews
import com.github.derleymad.lizwallet.repo.Repo
import com.github.derleymad.lizwallet.utils.convert24toTimestamp
import io.horizontalsystems.bitcoincore.BitcoinCore
import io.horizontalsystems.bitcoincore.core.IPublicKeyManager
import io.horizontalsystems.bitcoincore.core.IStorage
import io.horizontalsystems.bitcoincore.core.scriptType
import io.horizontalsystems.bitcoincore.extensions.toHexString
import io.horizontalsystems.bitcoincore.models.BalanceInfo
import io.horizontalsystems.bitcoincore.models.TransactionInfo
import io.horizontalsystems.bitcoincore.transactions.scripts.ScriptType
import io.horizontalsystems.bitcoincore.utils.AddressConverterChain
import io.horizontalsystems.bitcoinkit.BitcoinKit
import io.horizontalsystems.hdwalletkit.HDExtendedKey
import io.horizontalsystems.hdwalletkit.HDWallet
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(val app: Context, private val repo : Repo) : ViewModel() {

    private val _listOfCurrencies = MutableLiveData<ArrayList<ListOfCurrencies>?>()
    private val _newsRaw = MutableLiveData<RawNews?>()
    private val _market = MutableLiveData<ArrayList<MarketToRecyclerData>>()
    private val _fiatBrL = MutableLiveData<BitcoinToFiat>()

    val balance = MutableLiveData<Long?>()
    val transactionInfo = MutableLiveData<List<TransactionInfo>>()
    val synced = MutableLiveData(false)
    val stateBitcore = MutableLiveData<BitcoinCore.KitState>()
    val isLoading = MutableLiveData(false)
    val newReceiveAddress = MutableLiveData("")

    val currentCurrency= MutableLiveData<ListOfCurrencies>(null)
    val currentCurrencyDetails = MutableLiveData<CurrentCurrencyData>(null)
    val currentCurrencyHistoricalData = MutableLiveData<HistoricalData>(null)

    fun getCurrencies(){
        viewModelScope.launch {
            listOfCurrencies.postValue(repo.getCurrencies())

        }
    }

    fun getCurrentData(currency : String){
        viewModelScope.launch {
            currentCurrencyDetails.postValue(repo.getCurrentCurrencyData(currency))
        }
    }
    fun getCurrentCurrencyHistoricalData(id : String){
        viewModelScope.launch {

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MINUTE, -10)
            val now = calendar.timeInMillis / 1000

            val past = convert24toTimestamp()
            currentCurrencyHistoricalData.postValue(repo.getCurrentCurrencyHistoricalData(id,past,now))
        }
    }

    fun getNews() {
        viewModelScope.launch {
            val news=repo.getNews()
            newsRaw.postValue(news)

//            val options = TranslatorOptions.Builder()
//                .setSourceLanguage(TranslateLanguage.ENGLISH)
//                .setTargetLanguage(TranslateLanguage.PORTUGUESE)
//                .build()
//            val englishGermanTranslator = Translation.getClient(options)
//
//            var conditions = DownloadConditions.Builder()
//                .requireWifi()
//                .build()
//            englishGermanTranslator.downloadModelIfNeeded(conditions)
//                .addOnSuccessListener {
//                    for(i in 0..<news!!.data.size){
//                        englishGermanTranslator.translate(news.data[i].title).addOnSuccessListener { translated ->
//                            Log.i("translated_news",translated)
//                            news.data[i].title = translated
//                        }
//                        englishGermanTranslator.translate(news.data[i].body).addOnSuccessListener { translated ->
//                            Log.i("translated_news",translated)
//                            news.data[i].body = translated
//                        }
//                    }
//
//                }
//                .addOnFailureListener { exception ->
//                }
//            newsRaw.postValue(news)
        }
    }
    fun getBrlPrice() {
        viewModelScope.launch {
            fiatBrl.postValue(repo.getBrlPrice())
        }
    }
    fun getMarket() {
        viewModelScope.launch {
            market.postValue(repo.getMarket())
        }
    }


            fun initBitoinKit(walletName: String? = "") {
            isLoading.postValue(true)
            val sharedPreferences =
                app?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val xpub = sharedPreferences?.getString("xpub", null)

            if (xpub != null) {
                val context = app
                val extendedKey = HDExtendedKey(xpub)
                val bitcoinKit = BitcoinKit(
                    context = context,
                    extendedKey = extendedKey,
                    walletId = "$walletName",
                    syncMode = BitcoinCore.SyncMode.Api(),
                    networkType = BitcoinKit.NetworkType.MainNet,
                    confirmationsThreshold = 1,
                    purpose = HDWallet.Purpose.BIP84
                )
                bitcoinKit.listener = object : BitcoinKit.Listener {
                    override fun onBalanceUpdate(balance: BalanceInfo) {
                        this@HomeViewModel.balance.postValue(bitcoinKit.balance.spendable)
                        super.onBalanceUpdate(balance)
                    }

                    override fun onKitStateUpdate(state: BitcoinCore.KitState) {
                        if (state == BitcoinCore.KitState.Synced) {
                            Log.i("bitcoin testing", "synced")
                            synced.postValue(true)
                            isLoading.postValue(false)
                            val disposables = CompositeDisposable()
                            newReceiveAddress.postValue(bitcoinKit.receiveAddress())
                            bitcoinKit.transactions().subscribe { transactionInfos ->
                                this@HomeViewModel.transactionInfo.postValue(transactionInfos)
                                Log.i("transactioninfo", transactionInfos.toString())
                            }.let {
                                disposables.add(it)
                            }


                            val addressConverter = AddressConverterChain()
                            val path = bitcoinKit.getPublicKeyByPath("0/4").path
                            val publickey =bitcoinKit.receivePublicKey().publicKeyHash

//                            Log.i("wallet address" ,addressConverter.convert(publickey, HDWallet.Purpose.BIP84).stringValue)

                            try{

                                addressConverter.convert(publickey,ScriptType.P2PKH)
//                                Log.i("wallet address" ,bitcoinKit.getPublicKeyByPath("0/4").path)
                            }catch (e: Exception){
                                Log.i("wallet address", e.toString())
                            }


                        } else {
                            Log.i("bitcoin testing sync", state.toString())
                            stateBitcore.postValue(state)
                        }
                        super.onKitStateUpdate(state)
                    }

                }

                balance.postValue(bitcoinKit.balance.spendable)
                bitcoinKit.start()

            }

        }

    val listOfCurrencies: MutableLiveData<ArrayList<ListOfCurrencies>?> = _listOfCurrencies
    val newsRaw: MutableLiveData<RawNews?> = _newsRaw
    val market: MutableLiveData<ArrayList<MarketToRecyclerData>> = _market
    val fiatBrl: MutableLiveData<BitcoinToFiat> = _fiatBrL
}