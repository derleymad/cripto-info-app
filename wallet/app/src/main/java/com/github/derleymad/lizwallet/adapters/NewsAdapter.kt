package com.github.derleymad.lizwallet.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.model.news.Data
import com.github.derleymad.lizwallet.utils.converDataToBeaty
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Date

class NewsAdapter(
    private var onClickListener: (String) -> Unit
) :
    RecyclerView.Adapter<NewsAdapter.CurrencyViewHolder>() {

    private val localList = arrayListOf<Data>()

    @SuppressLint("NotifyDataSetChanged")
    fun insertListOfCurrenciesUpdated (localListOfCurrencies: ArrayList<Data>){
        localList.clear()
        localList.addAll(localListOfCurrencies)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
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
        fun bind(news: Data){
            val newsFont: TextView = itemView.findViewById(R.id.news_font)
            val newsTitle: TextView = itemView.findViewById(R.id.news_title)
            val newsDesc: TextView = itemView.findViewById(R.id.news_desc)
            val newsDate: TextView= itemView.findViewById(R.id.news_date)
            val newsImg: ImageView= itemView.findViewById(R.id.img_news_source)

            Picasso.get().load(news.imageurl).into(newsImg)
            newsFont.text = news.source_info.name
            newsTitle.text = news.title
            newsDesc.text = news.body
            newsDate.text = converDataToBeaty(news.published_on.toLong())

            itemView.setOnClickListener {
                onClickListener.invoke(news.url)
            }

        }
    }
}
