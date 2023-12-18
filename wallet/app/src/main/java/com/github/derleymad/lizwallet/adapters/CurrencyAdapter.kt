package com.github.derleymad.lizwallet.adapters

import android.annotation.SuppressLint
import android.content.res.Resources
import android.provider.CalendarContract.Colors
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.squareup.picasso.Picasso

class CurrencyAdapter(private val currencyList: List<ListOfCurrencies> = emptyList()) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private val localList = arrayListOf<ListOfCurrencies>()

    @SuppressLint("NotifyDataSetChanged")
    fun insertListOfCurrenciesUpdated (localListOfCurrencies: List<ListOfCurrencies>){
        localList.clear()
        localList.addAll(localListOfCurrencies)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = localList[position]
        holder.bind(currency)
    }

    override fun getItemCount(): Int {
        return localList.size
    }
    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(currency: ListOfCurrencies){

            val currencySymbolTextView: TextView = itemView.findViewById(R.id.currency_symbol)
            val currencyNameTextView: TextView = itemView.findViewById(R.id.currency_name)
            val currencyPriceTextView: TextView = itemView.findViewById(R.id.currency_price)
            val currencyRateTextView: TextView = itemView.findViewById(R.id.currency_rate)
            val currencyIconImageView : ImageView = itemView.findViewById(R.id.currency_image)

            Picasso.get().load(currency.image).into(currencyIconImageView)

            val variation = currency.price_change_percentage_24h
            val numeroFormatado = "%.2f".format(variation)

            when{
                variation<0->{currencyRateTextView.setTextColor(itemView.resources.getColor(R.color.red))}
                variation>0->{currencyRateTextView.setTextColor(itemView.resources.getColor(R.color.green))}
                variation== 0.toDouble() ->{currencyRateTextView.setTextColor(itemView.resources.getColor(R.color.white))}
            }

            currencyRateTextView.text = numeroFormatado+"%"

            currencySymbolTextView.text = currency.symbol.toUpperCase()
            currencyNameTextView.text = currency.name
            currencyPriceTextView.text = "$ "+currency.current_price.toString()

        }
    }
}
