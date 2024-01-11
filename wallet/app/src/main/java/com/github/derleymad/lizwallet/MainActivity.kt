package com.github.derleymad.lizwallet

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo

import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.github.derleymad.lizwallet.adapters.BottomViewPagerAdapter
import com.github.derleymad.lizwallet.databinding.ActivityMainBinding
import com.github.derleymad.lizwallet.network.RetrofitInstance
import com.github.derleymad.lizwallet.repo.Repo
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.ui.home.HomeViewModelFactory
import java.util.concurrent.Executor

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

    }

}
