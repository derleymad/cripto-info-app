package com.github.derleymad.lizwallet

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.github.derleymad.lizwallet.adapters.BottomViewPagerAdapter
import com.github.derleymad.lizwallet.databinding.ActivityMainBinding
import com.github.derleymad.lizwallet.network.RetrofitInstance
import com.github.derleymad.lizwallet.repo.Repo
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.ui.home.HomeViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeViewModel = ViewModelProvider(this, HomeViewModelFactory(
            this, Repo(this,RetrofitInstance)
        )
        )[HomeViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        val pagerAdapter = BottomViewPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPager.setCurrentItem(0,false)
                R.id.navigation_dashboard -> viewPager.setCurrentItem(1,false)
                R.id.navigation_notifications -> viewPager.setCurrentItem(2,false)
            }
            true
        }
        viewPager.setPageTransformer(null)
        viewPager.isUserInputEnabled = false

        homeViewModel.getCurrencies()
        homeViewModel.getNews()
        homeViewModel.getMarket()
        homeViewModel.getBrlPrice()
        homeViewModel.initBitoinKit()

    }
}



//        setContentView(binding.root)
//        val navView: BottomNavigationView = binding.navView
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//
////        setupActionBarWithNavController(navController, appBarConfiguration)
////        navView.setupWithNavController(navController)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
