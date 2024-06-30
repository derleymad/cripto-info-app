package com.github.derleymad.lizwallet.ui.wallets.wallet

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.adapters.AddressesAdapter
import com.github.derleymad.lizwallet.adapters.TransactionAdapter
import com.github.derleymad.lizwallet.databinding.FragmentWalletDetailsBinding
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.utils.converDataToBeaty
import com.github.derleymad.lizwallet.utils.converSaldoToBeaty
import com.github.derleymad.lizwallet.utils.extentions.navDetailsToAdress
import com.github.derleymad.lizwallet.utils.extentions.navMainToWalletDetails
import com.google.android.material.snackbar.Snackbar

class WalletDetailsFragment : Fragment() {


    private var _binding: FragmentWalletDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : TransactionAdapter
    private lateinit var adapterAddresses : AddressesAdapter

    val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWalletDetailsBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        homeViewModel.balance.observe(viewLifecycleOwner){
            binding.include2.saldoWallet.text = "${converSaldoToBeaty(it.toString()!!)} sats"
        }

        homeViewModel.transactionInfo.observe(viewLifecycleOwner){
            Log.i("testing add",it.toString())
            if(it.isNotEmpty()){
                val data = it[0].timestamp
                Log.i("testingtransaction", it[0].amount.toString())
                binding.include2.tvLastTransaction.text = converDataToBeaty(data)
                adapter.insertListOfCurrenciesUpdated(it)
            }
        }
        homeViewModel.newReceiveAddress.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                val arrayAdd = mutableListOf<String>()
                Log.i("testing add",it)
                arrayAdd.add(it)
                adapterAddresses.insertAddresses(arrayAdd)


            }
        }
       startRecyclerView(view)
    }



    private fun startRecyclerView(view: View) {
        adapter = TransactionAdapter()
        binding.rvTransactions.adapter = adapter
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())

        adapterAddresses = AddressesAdapter {
                Navigation.findNavController(view).navDetailsToAdress()
        }
        binding.rvAddresses.adapter = adapterAddresses
        binding.rvAddresses.layoutManager = LinearLayoutManager(requireContext())

    }



}