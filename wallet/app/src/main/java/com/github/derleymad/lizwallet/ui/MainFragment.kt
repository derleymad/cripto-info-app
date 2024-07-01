package com.github.derleymad.lizwallet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.adapters.BottomViewPagerAdapter
import com.github.derleymad.lizwallet.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = binding.viewPager

        val pagerAdapter = BottomViewPagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNavigation.menu.getItem(position).isChecked = true
            }
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> viewPager.setCurrentItem(0,false)
                R.id.navigation_portifolio-> viewPager.setCurrentItem(1,false)
//                R.id.navigation_dashboard -> viewPager.setCurrentItem(2,false)
                R.id.navigation_notifications -> viewPager.setCurrentItem(2,false)
            }
            true
        }
        viewPager.setPageTransformer(null)
        viewPager.isUserInputEnabled = false
    }

}