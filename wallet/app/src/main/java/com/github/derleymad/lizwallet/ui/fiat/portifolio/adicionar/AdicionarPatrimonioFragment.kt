package com.github.derleymad.lizwallet.ui.fiat.portifolio.adicionar

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.databinding.FragmentAddressBinding
import com.github.derleymad.lizwallet.databinding.FragmentAdicionarPatrimonioBinding
import com.github.derleymad.lizwallet.databinding.FragmentWalletsBinding
import com.github.derleymad.lizwallet.utils.extentions.navAddPortifilioToConectWatchOnly

class AdicionarPatrimonioFragment : Fragment() {

    private var _binding: FragmentAdicionarPatrimonioBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAdicionarPatrimonioBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.connectWalletBtc.setOnClickListener {
            Navigation.findNavController(view).navAddPortifilioToConectWatchOnly()
        }
        binding.back.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
    }
}