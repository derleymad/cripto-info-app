package com.github.derleymad.lizwallet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.squareup.picasso.Picasso

class AddressesAdapter(
    private var onClickListener : (String) -> Unit,
) :
    RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>() {

    private val localList = arrayListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun insertAddresses(localListOfAddresses: List<String>) {
        localList.clear()
        localList.addAll(localListOfAddresses)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder{
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.enderecos_item, parent, false)
        return AddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {

        val data = localList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int {
        // Adiciona um item extra para o card "ver mais"
        return localList.size
    }


    inner class AddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(address: String) {
            val textAddress = itemView.findViewById<TextView>(R.id.address)
            val number = itemView.findViewById<TextView>(R.id.number)
            textAddress.text = address
            number.text = "1"

            itemView.setOnClickListener {
                onClickListener.invoke(address)
            }
            itemView.findViewById<CardView>(R.id.container_status).setOnClickListener {
                onClickListener.invoke(address)
            }
            itemView.findViewById<TextView>(R.id.status).setOnClickListener {
                onClickListener.invoke(address)
            }
        }
    }

}
