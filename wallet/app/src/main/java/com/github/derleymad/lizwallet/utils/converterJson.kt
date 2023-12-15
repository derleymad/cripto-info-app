package com.github.derleymad.lizwallet.utils

import com.github.derleymad.lizwallet.model.ListOfCurrencies
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

fun convertObjectToJson(listOfCurrencies: ArrayList<ListOfCurrencies>): String {
    val gson = Gson()
    val type: Type = object : TypeToken<ArrayList<ListOfCurrencies>>() {}.type
    return gson.toJson(listOfCurrencies, type)
}