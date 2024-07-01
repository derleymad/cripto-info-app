package com.github.derleymad.lizwallet.ui.fiat.home

import android.animation.ValueAnimator
import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.adapters.PortifolioAdapter
import com.github.derleymad.lizwallet.adapters.TransactionAdapter
import com.github.derleymad.lizwallet.databinding.FragmentFiatControlBinding
import com.github.derleymad.lizwallet.databinding.FragmentPortifolioBinding
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.utils.converSaldoToBeaty
import com.github.derleymad.lizwallet.utils.extentions.navMainToWallet
import com.github.derleymad.lizwallet.utils.extentions.navPortifiloToAdd

class PortifolioFragment : Fragment() {

    private var _binding: FragmentPortifolioBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter : PortifolioAdapter

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPortifolioBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startRecyclerView(view)
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val xpub = sharedPreferences?.getString("xpub", null)

        if(xpub==null){
            binding.imageEmpty.visibility = View.VISIBLE
        }else{
            binding.imageEmpty.visibility = View.INVISIBLE
            adapter.insertIntoList(arrayListOf<String>("R$ 23.000"))
        }

        viewModel.balance.observe(viewLifecycleOwner){
            if(it!=null){
                val stringBeauty = converSaldoToBeaty(it.toString())
                adapter.insertIntoList(arrayListOf(stringBeauty) )
            }
        }

        binding.addButton.setOnClickListener {
            Navigation.findNavController(view).navPortifiloToAdd()
        }


    }


    private fun startRecyclerView(view: View) {
        adapter = PortifolioAdapter{
            Navigation.findNavController(view).navMainToWallet()
        }
        binding.rvListPortifolio.adapter = adapter
        binding.rvListPortifolio.layoutManager = LinearLayoutManager(requireContext())
    }

}