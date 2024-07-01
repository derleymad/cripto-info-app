package com.github.derleymad.lizwallet

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.derleymad.lizwallet.databinding.ActivityMainBinding
import com.github.derleymad.lizwallet.network.RetrofitInstance
import com.github.derleymad.lizwallet.repo.Repo
import com.github.derleymad.lizwallet.services.BitcoinService
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.ui.home.HomeViewModelFactory



class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeViewModel = ViewModelProvider(this, HomeViewModelFactory(
            this, Repo(this,RetrofitInstance)
        )
        )[HomeViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        homeViewModel.getCurrencies()
        homeViewModel.getNews()
        homeViewModel.getMarket()
        homeViewModel.getBrlPrice()
        homeViewModel.initBitoinKit()


//        val params = NetworkParameters.fromID("8333")
//// Provide the public key from which you want to derive addresses
//        val xPub =
//            "xpub6Cw8YA6Mko3xfkYpMQDZjGjgDTWUrJr87NBSiDPXqcmcSJTgxLXm3VCw3iQs4iC5ZrwpY3M21a43DZmiMzDXWzzhF1n7yxSXDnEHjJN6jwK"
//        val wallet = Wallet.fromWatchingKeyB58(params,xPub,DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS.toLong())
//// Create watching wallet, with the help of Wallet class
//
//// Print the very first derived address from provided public key
//        System.out.println("Receiving Address : " + wallet.currentReceiveAddress())
//        intent.putExtra("walletName", "derleyzim")
//        startService(intent)


    }

}
