package com.github.derleymad.lizwallet.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.github.derleymad.lizwallet.MainActivity
import com.github.derleymad.lizwallet.R
import io.horizontalsystems.bitcoincore.BitcoinCore
import io.horizontalsystems.bitcoincore.models.BalanceInfo
import io.horizontalsystems.bitcoincore.models.TransactionInfo
import io.horizontalsystems.bitcoinkit.BitcoinKit
import io.horizontalsystems.hdwalletkit.HDExtendedKey
import io.horizontalsystems.hdwalletkit.HDWallet
import io.reactivex.disposables.CompositeDisposable

class BitcoinService : Service() {

    private val isLoading = MutableLiveData<Boolean>()
    private val synced = MutableLiveData<Boolean>()
    private val stateBitcore = MutableLiveData<BitcoinCore.KitState>()
    private val balance = MutableLiveData<Long>()
    private val transactionInfo = MutableLiveData<List<TransactionInfo>>()
    private var disposables = CompositeDisposable()
    private lateinit var bitcoinKit: BitcoinKit

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "bitcoin_service"
            val channelName = "Bitcoin Background Service"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val walletName = intent?.getStringExtra("walletName") ?: ""
        startForegroundService()
        initBitcoinKit(walletName)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
        bitcoinKit.stop()
    }

    private fun initBitcoinKit(walletName: String) {
        isLoading.postValue(true)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val xpub = sharedPreferences.getString("xpub", null)

        if (xpub != null) {
            val extendedKey = HDExtendedKey(xpub)
            bitcoinKit = BitcoinKit(
                context = this,
                extendedKey = extendedKey,
                walletId = walletName,
                syncMode = BitcoinCore.SyncMode.Api(),
                networkType = BitcoinKit.NetworkType.MainNet,
                confirmationsThreshold = 1,
                purpose = HDWallet.Purpose.BIP84
            )
            bitcoinKit.listener = object : BitcoinKit.Listener {
                override fun onBalanceUpdate(balanceInfo: BalanceInfo) {
                    balance.postValue(bitcoinKit.balance.spendable)
                }

                override fun onKitStateUpdate(state: BitcoinCore.KitState) {
                    if (state == BitcoinCore.KitState.Synced) {
                        Log.i("bitcoin testing", "synced")
                        synced.postValue(true)
                        isLoading.postValue(false)
                        bitcoinKit.receiveAddress()
                        bitcoinKit.transactions().subscribe { transactionInfos ->
                            transactionInfo.postValue(transactionInfos)
                            Log.i("transactioninfo", transactionInfos.toString())
                        }.let {
                            disposables.add(it)
                        }
                        Log.i("wallet address", bitcoinKit.receiveAddress())
                    } else {
                        Log.i("bitcoin testing sync", state.toString())
                        stateBitcore.postValue(state)
                    }
                }
            }

            balance.postValue(bitcoinKit.balance.spendable)
            bitcoinKit.start()
        }
    }

    private fun startForegroundService() {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, "bitcoin_service")
            .setContentTitle("Bitcoin Service")
            .setContentText("Bitcoin service is running")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }
}
