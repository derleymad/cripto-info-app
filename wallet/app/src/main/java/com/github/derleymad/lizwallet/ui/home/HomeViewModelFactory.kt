package com.github.derleymad.lizwallet.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.derleymad.lizwallet.repo.Repo

class HomeViewModelFactory(private val app: Context, private val repo: Repo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(app, repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

