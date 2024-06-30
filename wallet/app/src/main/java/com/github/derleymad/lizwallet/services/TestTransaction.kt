package com.github.derleymad.lizwallet.services

import io.horizontalsystems.bitcoincore.BitcoinCore
import io.horizontalsystems.bitcoincore.core.DataProvider
import io.horizontalsystems.bitcoincore.core.IKitStateListener
import io.horizontalsystems.bitcoincore.models.BalanceInfo
import io.horizontalsystems.bitcoincore.models.BlockInfo
import io.horizontalsystems.bitcoincore.models.TransactionInfo
import io.horizontalsystems.bitcoincore.utils.AddressConverterChain

class TestTransaction (
    private val addressConverter: AddressConverterChain
) : IKitStateListener, DataProvider.Listener{
    override fun onBalanceUpdate(balance: BalanceInfo) {
        TODO("Not yet implemented")
    }

    override fun onLastBlockInfoUpdate(blockInfo: BlockInfo) {
        TODO("Not yet implemented")
    }

    override fun onTransactionsDelete(hashes: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onTransactionsUpdate(
        inserted: List<TransactionInfo>,
        updated: List<TransactionInfo>
    ) {
        TODO("Not yet implemented")
    }

    override fun onKitStateUpdate(state: BitcoinCore.KitState) {
        TODO("Not yet implemented")
    }
}