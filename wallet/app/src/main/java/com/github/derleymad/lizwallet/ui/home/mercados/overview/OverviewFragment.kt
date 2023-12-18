package com.github.derleymad.lizwallet.ui.home.mercados.overview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.adapters.CurrencyAdapter
import com.github.derleymad.lizwallet.adapters.MarketAdapter
import com.github.derleymad.lizwallet.databinding.FragmentOverviewBinding
import com.github.derleymad.lizwallet.model.market.MarketData
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OverviewFragment : Fragment() {

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
        binding.included.shimmerViewContainer.startShimmer()

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.included.shimmerViewContainer.stopShimmer()
            binding.rvMarket.visibility = View.VISIBLE
            binding.cvCurrencies.visibility = View.VISIBLE
            binding.included.root.visibility = View.INVISIBLE
        }, 2000)



        homeViewModel.listOfCurrencies.observe(viewLifecycleOwner) {
            adapter.insertListOfCurrenciesUpdated(it.take(4))
        }
        homeViewModel.market.observe(viewLifecycleOwner){
            marketAdapter.insertListOfCurrenciesUpdated(it)
        }

    }

    fun startRecyclerViews(){
        //market
        marketAdapter = MarketAdapter{}
        binding.rvMarket.adapter =marketAdapter
        binding.rvMarket.layoutManager = GridLayoutManager(requireContext(),2)

        //currencies
        adapter = CurrencyAdapter()
        binding.rvCurrencies.adapter = adapter
        val linearLayout = object : LinearLayoutManager(requireContext()) { override fun canScrollVertically() = false }
        binding.rvCurrencies.layoutManager = linearLayout

    }
}