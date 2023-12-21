package com.github.derleymad.lizwallet.ui.home

import BitcoinToFiat
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.github.derleymad.lizwallet.model.market.MarketToRecyclerData
import com.github.derleymad.lizwallet.model.news.RawNews
import com.github.derleymad.lizwallet.repo.Repo
import io.horizontalsystems.bitcoincore.BitcoinCore
import io.horizontalsystems.bitcoincore.models.BalanceInfo
import io.horizontalsystems.bitcoincore.models.TransactionInfo
import io.horizontalsystems.bitcoinkit.BitcoinKit
import io.horizontalsystems.hdwalletkit.HDExtendedKey
import io.horizontalsystems.hdwalletkit.HDWallet
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

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

    fun getCurrencies(){
        viewModelScope.launch {
            listOfCurrencies.postValue(repo.getCurrencies())

        }
    }
    fun getNews() {
        viewModelScope.launch {
            newsRaw.postValue(repo.getNews())
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
                    confirmationsThreshold = 6,
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
                            bitcoinKit.transactions().subscribe { transactionInfos ->
                                this@HomeViewModel.transactionInfo.postValue(transactionInfos)
                                Log.i("transactioninfo", transactionInfos.toString())
                            }.let {
                                disposables.add(it)
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