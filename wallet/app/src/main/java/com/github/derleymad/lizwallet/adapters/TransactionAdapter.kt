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
import com.github.derleymad.lizwallet.utils.converDataToBeaty
import com.github.derleymad.lizwallet.utils.converSaldoToBeaty
import com.squareup.picasso.Picasso
import io.horizontalsystems.bitcoincore.models.TransactionInfo

class TransactionAdapter() :
    RecyclerView.Adapter<TransactionAdapter.CurrencyViewHolder>() {

    private val localList = arrayListOf<TransactionInfo>()

    @SuppressLint("NotifyDataSetChanged")
    fun insertListOfCurrenciesUpdated (localList: List<TransactionInfo>){
        this.localList.clear()
        this.localList.addAll(localList)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction_item, parent, false)
        return CurrencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val transactionInfo = localList[position]
        holder.bind(transactionInfo)
    }

    override fun getItemCount(): Int {
        return localList.size
    }
    class CurrencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data : TransactionInfo){

            val dateOfTransaction: TextView = itemView.findViewById(R.id.tv_transaction_date)
            val amoutOfTransaction : TextView = itemView.findViewById(R.id.tv_transaction_amout)
            val imgOfTransaction : ImageView = itemView.findViewById(R.id.img_transaction_type)

            when{
                data.type.value == 1 ->{
                    amoutOfTransaction.apply {
                        setTextColor(itemView.resources.getColor(R.color.green))
                        text="+"+converSaldoToBeaty(data.amount)+" sats"
                    }
                }

                data.type.value == 2->{
                    amoutOfTransaction.apply {
                        setTextColor(itemView.resources.getColor(R.color.red))
                        text="-"+converSaldoToBeaty(data.amount)+" sats"
                    }
                    val drawable = itemView.resources.getDrawable(R.drawable.baseline_send_24)
                    imgOfTransaction.setImageDrawable(drawable)
                }

                data.type.value == 3->{
                    amoutOfTransaction.apply {
                        setTextColor(itemView.resources.getColor(R.color.green))
                        text ="+"+converSaldoToBeaty(data.amount)+" sats"
                    }
                }

            }

            dateOfTransaction.text = converDataToBeaty(data.timestamp)

        }
    }
}
