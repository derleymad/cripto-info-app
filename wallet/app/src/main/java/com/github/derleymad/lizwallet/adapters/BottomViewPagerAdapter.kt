package com.github.derleymad.lizwallet.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.derleymad.lizwallet.ui.home.HomeFragment
import com.github.derleymad.lizwallet.ui.settings.SettingsFragment
import com.github.derleymad.lizwallet.ui.wallets.WalletsFragment

class BottomViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3 // Número de fragmentos

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> WalletsFragment()
            2 -> SettingsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
