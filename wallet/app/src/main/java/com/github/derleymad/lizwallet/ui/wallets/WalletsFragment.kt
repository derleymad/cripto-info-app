package com.github.derleymad.lizwallet.ui.wallets

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.adapters.TransactionAdapter
import com.github.derleymad.lizwallet.databinding.FragmentWalletsBinding
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.ui.wallets.bottomsheet.BottomSheetFragment
import com.github.derleymad.lizwallet.utils.converDataToBeaty
import com.github.derleymad.lizwallet.utils.converSaldoToBeaty
import com.github.derleymad.lizwallet.utils.extentions.navMainToWalletDetails
import com.google.android.material.snackbar.Snackbar
import java.io.File

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
    fun clearAppData(context: Context) {
        val cacheDir = context.cacheDir
        val appDir = context.filesDir.parentFile

        deleteDir(cacheDir)
        deleteDir(appDir)
    }
    fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        // Agora o diretório está vazio e pode ser excluído
        return dir?.delete() ?: false
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
    private fun showConfirmationDialog(textToCopy: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmação de exclusão")
        builder.setMessage("Deseja realmente excluir sua carteira Watch Only?")

        builder.setPositiveButton("Sim") { dialog, which ->
            clearAppData(requireContext())
        }

        builder.setNegativeButton("Não") { dialog, which ->
        }

        val dialog = builder.create()
        dialog.show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val xpub = sharedPreferences?.getString("xpub", null)
        Log.i("xpubinfo",xpub.toString())

        if(xpub==null){
            binding.addWalletButton.visibility = View.VISIBLE
            binding.removeWalletButton.visibility = View.GONE
        }else{
            binding.addWalletButton.visibility = View.INVISIBLE
            binding.removeWalletButton.visibility = View.VISIBLE
        }
        binding.addWalletButton.setOnClickListener {
//            homeViewModel.watchOnlyAdress.postValue()
            val bottomSheetFragment = BottomSheetFragment()
            bottomSheetFragment.show(requireFragmentManager(), bottomSheetFragment.tag)
        }

        binding.removeWalletButton.setOnClickListener {
            showConfirmationDialog("")
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
        binding.include.cardWallet.setOnClickListener{
            Navigation.findNavController(view).navMainToWalletDetails()
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