package com.github.derleymad.lizwallet.ui.wallets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.adapters.TransactionAdapter
import com.github.derleymad.lizwallet.databinding.FragmentWalletsBinding
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.ui.wallets.bottomsheet.BottomSheetFragment
import com.github.derleymad.lizwallet.utils.converDataToBeaty
import com.github.derleymad.lizwallet.utils.converSaldoToBeaty
import com.github.derleymad.lizwallet.utils.extentions.navMainToWalletDetails
import com.google.android.material.snackbar.Snackbar

class WalletsFragment : Fragment() {

    private var _binding: FragmentWalletsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : TransactionAdapter

    val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalletsBinding.inflate(inflater, container, false)

        startRecyclerView()
        Log.i("view","creating")
        val root: View = binding.root
        return root
    }

    private fun startRecyclerView() {
        adapter = TransactionAdapter()
        binding.rvTransactions.adapter = adapter
        binding.rvTransactions.layoutManager = LinearLayoutManager(requireContext())
    }

    fun extrairParteNumerica(s: String): Double? {
        val regex = """(\d+(\.\d+)?)""".toRegex()
        val matchResult = regex.find(s)
        return matchResult?.groupValues?.get(1)?.toDoubleOrNull()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.include.cardWallet.setOnClickListener{
            Navigation.findNavController(view).navMainToWalletDetails()
        }

        binding.addWalletButton.setOnClickListener {
//            homeViewModel.watchOnlyAdress.postValue()
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(requireFragmentManager(), bottomSheetFragment.tag)

        }
        homeViewModel.stateBitcore.observe(viewLifecycleOwner){
            Log.i("testing",it.toString())
            if(it.toString().contains("NoInternet")){
                Snackbar.make(binding.root,"Sem conexão com a internet, os dados podem estar desatualizados...",Snackbar.LENGTH_LONG).show()
            }
            if(it.toString().contains("Syncing")){
            val progress = extrairParteNumerica(it.toString())
            if(progress!=0.0){
            binding.syncProgress.text = (progress!!*100).toInt().toString()+"% ...sincronizando com a blockchain"
            }
            }

        }

        binding.include.saldoWallet.setOnClickListener {
            changeCurrencySaldo()
        }

        homeViewModel.balance.observe(viewLifecycleOwner){
            binding.include.saldoWallet.text = "${converSaldoToBeaty(it.toString()!!)} sats"
        }

        homeViewModel.transactionInfo.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                val data = it[0].timestamp
                Log.i("testingtransaction", it[0].amount.toString())
                binding.include.tvLastTransaction.text = converDataToBeaty(data)
                adapter.insertListOfCurrenciesUpdated(it)
            }
        }


    }

    private fun changeCurrencySaldo() {
//        homeViewModel.balance.postValue(homeViewModel.balance.value!!/100000000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}