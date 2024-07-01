package com.github.derleymad.lizwallet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.model.news.Data
import com.github.derleymad.lizwallet.utils.converDataToBeaty
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class PortifolioAdapter(
    private var onClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<PortifolioAdapter.CurrencyViewHolder>() {

    private val localList = arrayListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun insertIntoList(localList: ArrayList<String>){
        this.localList.clear()
        this.localList.addAll(localList)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.portifolio_item, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val news = localList[position]
        holder.bind(news)
    }

    override fun getItemCount(): Int {
        return localList.size
    }
    inner class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data : String){

            val saldo : TextView = itemView.findViewById(R.id.current_saldo)
            val container = itemView.findViewById<ConstraintLayout>(R.id.container_click)

            saldo.text = data

            container.setOnClickListener {
                //AKI VAI UMA DATA PARA A URL PARA ONDE O ITEM IRA NAVEGAR
                onClickListener.invoke("btc")
            }

        }
    }
}
