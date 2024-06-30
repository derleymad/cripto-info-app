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
        val intent = Intent(this, BitcoinService::class.java)
//        intent.putExtra("walletName", "derleyzim")
//        startService(intent)


    }

}
