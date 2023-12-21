package com.github.derleymad.lizwallet.ui.home.mercados.overview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.adapters.CurrencyAdapter
import com.github.derleymad.lizwallet.databinding.ActivityCurrenciesBinding
import com.github.derleymad.lizwallet.network.RetrofitInstance
import com.github.derleymad.lizwallet.repo.Repo
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.ui.home.HomeViewModelFactory

class CurrenciesActivity : AppCompatActivity() {
    lateinit var binding :ActivityCurrenciesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrenciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val homeViewModel = ViewModelProvider(this, HomeViewModelFactory(
            this, Repo(this, RetrofitInstance)
        )
        )[HomeViewModel::class.java]
        homeViewModel.getCurrencies()
        homeViewModel.listOfCurrencies.observe(this){
            val adapter = CurrencyAdapter({},{})
            binding.rvAllCurrencies.adapter = adapter
            if (it != null) {
                adapter.insertListOfCurrenciesUpdated(it.toList())
            }
            binding.rvAllCurrencies.layoutManager = LinearLayoutManager(this)
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return true
    }
}