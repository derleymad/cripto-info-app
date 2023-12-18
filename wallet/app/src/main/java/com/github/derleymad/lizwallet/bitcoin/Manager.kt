package com.github.derleymad.lizwallet.bitcoin

import android.util.Log
import com.github.derleymad.lizwallet.ui.wallets.WalletsFragment
import io.horizontalsystems.bitcoincore.BitcoinCore
import io.horizontalsystems.bitcoincore.models.BalanceInfo
import io.horizontalsystems.bitcoincore.models.BlockInfo
import io.horizontalsystems.bitcoincore.models.TransactionInfo
import io.horizontalsystems.bitcoinkit.BitcoinKit

class Manager(bitcoinKit: BitcoinKit, private val callBack : WalletsFragment) : BitcoinKit.Listener {
    init {
        bitcoinKit.listener = this
    }
    interface Callback{
        fun onPreExecute()
        fun onResult(result : String)
        fun onResultTransactions(result: List<TransactionInfo>)
        fun onFailure(message: String)
    }


    override fun onBalanceUpdate(balance: BalanceInfo) {
        Log.i("bitcoin balance update ->",(balance.spendable/100000000).toString())
//        callBack.onResult(balance.toString())

    }

    override fun onLastBlockInfoUpdate(blockInfo: BlockInfo) {
//        Log.i("bitcoin blocklast update ->",blockInfo.toString())
    }

    override fun onKitStateUpdate(state: BitcoinCore.KitState) {
    }

    override fun onTransactionsUpdate(inserted: List<TransactionInfo>, updated: List<TransactionInfo>) {
        Log.i("bitcoin trasaction inserted->",inserted.toString())
        Log.i("bitcoin trasaction updated ->",updated.toString())
//        callBack.onResultTransactions(inserted)
    }

    override fun onTransactionsDelete(hashes: List<String>) {
    }

}