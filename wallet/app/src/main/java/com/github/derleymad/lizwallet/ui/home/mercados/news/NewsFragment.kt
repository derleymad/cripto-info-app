package com.github.derleymad.lizwallet.ui.home.mercados.news

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.adapters.CurrencyAdapter
import com.github.derleymad.lizwallet.adapters.NewsAdapter
import com.github.derleymad.lizwallet.databinding.FragmentNewsBinding
import com.github.derleymad.lizwallet.databinding.FragmentOverviewBinding
import com.github.derleymad.lizwallet.ui.home.HomeViewModel

class NewsFragment : Fragment() {

    companion object {
        fun newInstance() = NewsFragment()
    }

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter : NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeViewModel: HomeViewModel by activityViewModels()
        homeViewModel.newsRaw.observe(viewLifecycleOwner){ it ->
            Log.i("testing","chegou ${it.toString()}")

            adapter = NewsAdapter{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(it)
                startActivity(intent)
            }

            binding.rvNews.adapter = adapter
            binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
            adapter.insertListOfCurrenciesUpdated(it.data)
        }

    }

}