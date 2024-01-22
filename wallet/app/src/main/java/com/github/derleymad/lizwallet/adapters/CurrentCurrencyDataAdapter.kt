package com.github.derleymad.lizwallet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.model.CurrentCurrencyDataBasic
import com.github.derleymad.lizwallet.utils.formatarNumero

class CurrentCurrencyDataAdapter(
    private var onClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<CurrentCurrencyDataAdapter.CurrencyViewHolder>() {

    private val localList = arrayListOf<CurrentCurrencyDataBasic>()

    @SuppressLint("NotifyDataSetChanged")
    fun insertListOfCurrenciesUpdated (localListOfData: ArrayList<CurrentCurrencyDataBasic>){
        localList.clear()
        localList.addAll(localListOfData)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.current_data_item, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val data = localList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        return localList.size
    }
    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CurrentCurrencyDataBasic){
            val itemName: TextView = itemView.findViewById(R.id.item_name)
            val itemData: TextView = itemView.findViewById(R.id.item_data)

//            Picasso.get().load(news.imageurl).into(newsImg)

            itemName.text = item.name
            itemData.text = formatarNumero(item.data.toDouble())

            itemView.setOnClickListener {
            }

        }
    }
}
