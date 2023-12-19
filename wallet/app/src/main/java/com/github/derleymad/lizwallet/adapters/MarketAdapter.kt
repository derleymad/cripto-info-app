package com.github.derleymad.lizwallet.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.model.market.MarketData
import com.github.derleymad.lizwallet.model.market.MarketToRecyclerData
import com.github.derleymad.lizwallet.utils.chart.LineChartView
import com.github.derleymad.lizwallet.utils.formatarNumero
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class MarketAdapter(
    private var onClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<MarketAdapter.CurrencyViewHolder>() {

    private val localList = arrayListOf<MarketToRecyclerData>()

    @SuppressLint("NotifyDataSetChanged")
    fun insertListOfCurrenciesUpdated (list: ArrayList<MarketToRecyclerData>){
        localList.clear()
        localList.addAll(list)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.market_item, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val market = localList[position]
        holder.bind(market)
    }

    override fun getItemCount(): Int {
        return localList.size
    }
    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(market: MarketToRecyclerData){

            val marketPrice : TextView = itemView.findViewById(R.id.market_price)
            val marketName: TextView = itemView.findViewById(R.id.market_name)
            val marketPercentage: TextView = itemView.findViewById(R.id.tv_percentage)
            val lineChart : LineChart= itemView.findViewById(R.id.lineChart)
            val marketDataList: List<Double> = market.marketData

            marketName.text = market.name


            val entries = mutableListOf<Entry>()
            for ((index, marketData) in marketDataList.withIndex()) {
                val entry = Entry(index.toFloat(), marketData.toFloat())
                entries.add(entry)
            }

            val dataSet = LineDataSet(entries, "Label para o conjunto de dados")

            val green = itemView.resources.getColor(R.color.green)
            val red = itemView.resources.getColor(R.color.red)

            when{
               market.percentage>0 -> {
                   dataSet.color = green
                   marketPercentage.setTextColor(green)}
               market.percentage<0 -> {
                   dataSet.color = red
                   marketPercentage.setTextColor(red)}
            }

            val formattedPercentage = String.format("%.2f", market.percentage)
            marketPercentage.text = formattedPercentage+"%"
            marketPrice.text = "$"+formatarNumero(market.value)



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
            lineChart.xAxis.isEnabled = false // desativa o eixo x

            lineChart.setTouchEnabled(false)
            lineChart.setDragEnabled(false)
            lineChart.setScaleEnabled(false)
            lineChart.setPinchZoom(false)
            lineChart.invalidate() // atualiza o gráfico

            itemView.setOnClickListener {
//                onClickListener.invoke(news.)
            }

        }
    }
}
