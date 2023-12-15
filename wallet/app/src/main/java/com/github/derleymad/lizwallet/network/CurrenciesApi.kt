package com.github.derleymad.lizwallet.network
import com.github.derleymad.lizwallet.model.ListOfCurrencies
import retrofit2.Call
import retrofit2.http.GET

interface CurrenciesApi{
    @GET("markets?vs_currency=USD&order=market_cap_desc&per_page=20&page=1&sparkline=false&price_change_percentage=24h,7d,14d,30d,200d,1y")
    fun getListOfCurrencies(): Call<ArrayList<ListOfCurrencies>>

//    @GET("destaques.json")
//    fun getDestaques(): Call<ArrayList<Comida>>
//
//    @GET("nova_categorias.json")
//    fun getNovaCategorias(): Call<Categorias>
}