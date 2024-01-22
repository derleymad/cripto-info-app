package com.github.derleymad.lizwallet.ui.home.mercados.news

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.adapters.NewsAdapter
import com.github.derleymad.lizwallet.databinding.FragmentNewsBinding
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.derleymad.lizwallet.ui.home.news.WebViewActivity

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : NewsAdapter

    val homeViewModel: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        homeViewModel.newsRaw.observe(viewLifecycleOwner){ newsRaw->

            if (newsRaw!= null) {
                adapter = NewsAdapter{
//                    val intent = Intent(Intent.ACTION_VIEW)
//                    intent.data = Uri.parse(it)
                    val intent = Intent(requireContext(), WebViewActivity::class.java)
                    intent.putExtra(WebViewActivity.EXTRA_LINK, it)
                    startActivity(intent)
                }
                binding.rvNews.adapter = adapter
                binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
                adapter.insertListOfCurrenciesUpdated(newsRaw.data)
            }
        }
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}