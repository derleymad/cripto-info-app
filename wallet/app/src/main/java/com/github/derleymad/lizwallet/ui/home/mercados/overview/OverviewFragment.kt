package com.github.derleymad.lizwallet.ui.home.mercados.overview

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.adapters.CurrencyAdapter
import com.github.derleymad.lizwallet.databinding.FragmentHomeBinding
import com.github.derleymad.lizwallet.databinding.FragmentOverviewBinding
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.ui.home.HomeViewModelFactory

class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : CurrencyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeViewModel: HomeViewModel by activityViewModels()

        homeViewModel.listOfCurrencies.observe(viewLifecycleOwner) {
            Log.i("testing","chegou ${it.toString()}")
            adapter = CurrencyAdapter()
            binding.rvCurrencies.adapter = adapter
            binding.rvCurrencies.layoutManager = LinearLayoutManager(requireContext())
            adapter.insertListOfCurrenciesUpdated(it)
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}