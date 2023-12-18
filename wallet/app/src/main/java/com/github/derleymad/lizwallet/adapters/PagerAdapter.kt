package com.github.derleymad.lizwallet.adapters

import android.content.res.Resources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.derleymad.lizwallet.ui.home.favorites.FavoritesFragment
import com.github.derleymad.lizwallet.ui.home.mercados.news.NewsFragment
import com.github.derleymad.lizwallet.ui.home.mercados.overview.OverviewFragment

class PagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                OverviewFragment()
            }
            1 -> {
                NewsFragment()
            }
            2 -> {
                FavoritesFragment()
            }
            else -> {
                throw Resources.NotFoundException("Posição nao foi achada!")
            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}