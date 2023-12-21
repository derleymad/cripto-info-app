package com.github.derleymad.lizwallet.ui.home.mercados.overview

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.MainActivity
import com.github.derleymad.lizwallet.adapters.CurrencyAdapter
import com.github.derleymad.lizwallet.adapters.MarketAdapter
import com.github.derleymad.lizwallet.databinding.FragmentOverviewBinding
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.utils.converSaldoToBeaty
import com.github.derleymad.lizwallet.utils.onclicks.SeeMoreOnClickListener
import com.google.android.material.snackbar.Snackbar

class OverviewFragment : Fragment(), SeeMoreOnClickListener {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var adapter : CurrencyAdapter
    private lateinit var marketAdapter: MarketAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        startRecyclerViews()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.included.shimmerViewContainer.stopShimmer()
            binding.rvMarket.visibility = View.VISIBLE
            binding.cvCurrencies.visibility = View.VISIBLE
            binding.included.root.visibility = View.INVISIBLE
        }, 2000)

        binding.included.shimmerViewContainer.startShimmer()
        Log.i("lifecycle","viewcreate")

        val root: View = binding.root
        return root
    }

    private fun refreshOverview() {
        homeViewModel.getCurrencies()
        homeViewModel.getNews()
        homeViewModel.getBrlPrice()
        homeViewModel.getMarket()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("created","createeted")

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshOverview()
        }

        homeViewModel.listOfCurrencies.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.insertListOfCurrenciesUpdated(it.take(5))
            }else{
                Snackbar.make(binding.root,"Sem conexão com a internet",Snackbar.LENGTH_LONG).show()
            }
        }
        homeViewModel.market.observe(viewLifecycleOwner){
            if(it!=null){
                marketAdapter.insertListOfCurrenciesUpdated(it)
            }
        }
        binding.checkSaldo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.saldoFiatSpot.visibility = View.GONE
            } else {
                binding.saldoFiatSpot.visibility = View.VISIBLE
            }
        }

        homeViewModel.fiatBrl.observe(viewLifecycleOwner){ bitcoinToFiat ->
            if(bitcoinToFiat != null){
                homeViewModel.balance.observe(viewLifecycleOwner){
                    if (it != null) {

//                        if(saldoAtual != it){
//                        }

                        binding.saldoContainer.visibility = View.VISIBLE
                        val satsToBtc = it.toFloat().div(100000000)
                        val btcToBrL =  (satsToBtc*bitcoinToFiat.bitcoin.brl)

//                        Log.i("saldo","antigo ${saldoAtual!!.toInt()}")
//                        Log.i("saldo","novo ${btcToBrL.toInt()}")

                        val animator = ValueAnimator.ofFloat(0f,btcToBrL)
                        animator.duration = 1500

                        animator.addUpdateListener { animation ->
                            val saldoAnimado = animation.animatedValue as Float
                            val stringBeauty = converSaldoToBeaty(saldoAnimado.toString())
                        binding.saldoFiatSpot.text = "R$ "+ stringBeauty
                        }
                        animator.start()
                    }
                    else{
                        binding.saldoContainer.visibility = View.GONE
                    }
                }
            }
        }
    }

    fun startRecyclerViews(){
        //market
        marketAdapter = MarketAdapter{}
        binding.rvMarket.adapter =marketAdapter
        val gridLayout = object : GridLayoutManager(requireContext(),2) { override fun canScrollVertically() = false }
        binding.rvMarket.layoutManager = gridLayout

        //currencies
        adapter = CurrencyAdapter({},{
            val intent = Intent(requireContext(),CurrenciesActivity::class.java)
            startActivity(intent)
        })

        binding.rvCurrencies.adapter = adapter
        val linearLayout = object : LinearLayoutManager(requireContext()) { override fun canScrollVertically() = false }
        binding.rvCurrencies.layoutManager = linearLayout

    }

    override fun onDestroy() {
        Log.i("ondestroy","destroy")
        _binding = null
        super.onDestroy()
    }

    override fun onClick() {
    }
}