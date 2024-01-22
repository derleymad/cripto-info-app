package com.github.derleymad.lizwallet.ui.home.mercados.currency

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.adapters.CurrentCurrencyDataAdapter
import com.github.derleymad.lizwallet.databinding.FragmentCurrencyBinding
import com.github.derleymad.lizwallet.model.CurrentCurrencyDataBasic
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.squareup.picasso.Picasso

class CurrencyFragment : Fragment() {

    val homeViewModel: HomeViewModel by activityViewModels()

    private var _binding: FragmentCurrencyBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : CurrentCurrencyDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrencyBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun changeDataToRecyclerView(data : ListOfCurrencies) : ArrayList<CurrentCurrencyDataBasic>{
        val list = arrayListOf<CurrentCurrencyDataBasic>()
        list.add(
            CurrentCurrencyDataBasic(
            name = "Capitalização de mercado",
            data = data.market_cap.toString()
        ))

        list.add(CurrentCurrencyDataBasic(
            name = "Fornecimento total",
            data = data.max_supply.toString()
        ))

        list.add(CurrentCurrencyDataBasic(
            name = "Em Circulação",
            data = data.market_cap.toString()
        ))

        list.add(CurrentCurrencyDataBasic(
            name = "Volume de Operações",
            data = data.total_volume.toString()
        ))

        list.add(CurrentCurrencyDataBasic(
            name = "Mercado diluído",
            data = data.market_cap_change_24h.toString()
        ))

        return list
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.currentCurrency.observe(viewLifecycleOwner){
            binding.currencyName.text = it.name
            Picasso.get().load(it.image).into(binding.imgCurrency)
            binding.currencyPrice.text = it.current_price.toString()

            val price24Change = "%.2f".format(it.price_change_percentage_24h)

            binding.priceCurrencyChange.text = price24Change+"%"
            binding.currencyRank.text = "#"+it.market_cap_rank.toString()

            //recyclerview
            adapter = CurrentCurrencyDataAdapter {}
            binding.rvCurrencyData.adapter = adapter
            binding.rvCurrencyData.layoutManager = LinearLayoutManager(requireContext())
            adapter.insertListOfCurrenciesUpdated(changeDataToRecyclerView(it))

            homeViewModel.getCurrentData(it.id)

            homeViewModel.getCurrentCurrencyHistoricalData(it.id)
        }

        ///TODO
        homeViewModel.currentCurrencyDetails.observe(viewLifecycleOwner){
            if(it!=null){
                Log.i("testing details",it.toString())
                binding.tvVisaoGeral.text = Html.fromHtml(it.description.en.toString())
            }
        }

        homeViewModel.currentCurrencyHistoricalData.observe(viewLifecycleOwner){
            val lineChart = binding.lineChartCurrency
//            val historicalDataList: List<Double> =
            var historicalDataList : List<Double> = arrayListOf()

            if(it!=null){
                Log.i("testing2",it.toString())
                val tempList = mutableListOf<Double>()
                for(i in it.prices){
                    tempList.add(i[1].toDouble())
                }
                historicalDataList = tempList

                Log.i("testinghi",historicalDataList.toString())

                val entries = mutableListOf<Entry>()
                for ((index, marketData) in historicalDataList.withIndex()) {
                    val entry = Entry(index.toFloat(), marketData.toFloat())
                    entries.add(entry)
                }

                val dataSet = LineDataSet(entries, "Label para o conjunto de dados")

                val green = resources.getColor(R.color.green)
                val red = resources.getColor(R.color.red)

                dataSet.color = green

//            when{
//                market.percentage>0 -> {
//                    dataSet.color = green
//                    marketPercentage.setTextColor(green)}
//                market.percentage<0 -> {
//                    dataSet.color = red
//                    marketPercentage.setTextColor(red)}
//            }

//            val formattedPercentage = String.format("%.2f", market.percentage)
//            marketPercentage.text = formattedPercentage+"%"
//            marketPrice.text = "$"+ formatarNumero(market.value)

//
//                val maxValue = historicalDataList.maxOrNull()
//                val minValue = historicalDataList.minOrNull()
//
//// Adicione linhas verticais
//                if (maxValue != null) {
//                    val maxValueEntry = Entry(entries.indexOfFirst { it.y == maxValue.toFloat() }.toFloat(), maxValue.toFloat())
//                    lineChart.xAxis.addLimitLine(LimitLine(maxValue.toFloat(), "Max: $maxValue"))
//                }
//
//                if (minValue != null) {
//                    val minValueEntry = Entry(entries.indexOfFirst { it.y == minValue.toFloat() }.toFloat(), minValue.toFloat())
//                    lineChart.xAxis.addLimitLine(LimitLine(minValue.toFloat(), "Min: $minValue"))
//                }

                dataSet.setDrawCircles(false) // não desenha os pontos circulares
                dataSet.setDrawFilled(false) // não preenche a área abaixo da linha
                dataSet.setDrawValues(false) // não exibe os valores nos pontos

                val dataSets: ArrayList<ILineDataSet> = ArrayList()
                dataSets.add(dataSet)

                val lineData = LineData(dataSets)

                lineChart.data = lineData

                lineChart.description.isEnabled = false // desativa a descrição (label) do gráfico
                lineChart.legend.isEnabled = false // desativa a legenda
                lineChart.axisLeft.isEnabled = false // desativa o eixo y da esquerda
                lineChart.axisRight.isEnabled = false // desativa o eixo y da direita
                lineChart.xAxis.isEnabled = false// desativa o eixo x

                lineChart.setTouchEnabled(false)
                lineChart.setDragEnabled(false)
                lineChart.setScaleEnabled(false)
                lineChart.setPinchZoom(false)
//                lineChart.xAxis.addLimitLine(LimitLine(40000.toFloat(), "Max: $40.000"))
                lineChart.invalidate() // atualiza o gráfico

            }



//            Log.i("testinghistorical",it.toString())
//            it.prices.map{
////                historicalDataList.add(it[0].toDouble())
//            }





        }



        binding.back.setOnClickListener {
//            Navigation.findNavController(view).popBackStack()
            findNavController().popBackStack()
        }

    }

}