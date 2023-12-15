package com.github.derleymad.lizwallet.ui.home

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.adapters.CurrencyAdapter
import com.github.derleymad.lizwallet.adapters.PagerAdapter
import com.github.derleymad.lizwallet.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private fun setupTabLayout() {
        binding.apply {
            viewPager.adapter =
                PagerAdapter(requireActivity())
            viewPager.offscreenPageLimit = 4
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    0 -> "Visão Geral"
                    1 -> "Notícias"
                    2 -> "Favoritos"
                    else -> throw  Resources.NotFoundException("Posição não encontrada!")
                }
            }.attach()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel = ViewModelProvider(this, HomeViewModelFactory(
//            requireContext(),
//
//        )).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupTabLayout()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}