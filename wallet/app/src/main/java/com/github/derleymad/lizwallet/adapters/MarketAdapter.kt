package com.github.derleymad.lizwallet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.model.market.MarketData
import com.github.derleymad.lizwallet.model.market.MarketToRecyclerData
import com.github.derleymad.lizwallet.utils.formatarNumero

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

            marketName.text = market.name
            marketPrice.text = "$"+formatarNumero(market.value)

            itemView.setOnClickListener {
//                onClickListener.invoke(news.)
            }

        }
    }
}
