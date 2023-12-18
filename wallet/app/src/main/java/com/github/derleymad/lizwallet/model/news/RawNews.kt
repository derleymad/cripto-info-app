package com.github.derleymad.lizwallet.model.news

import Source_info
import com.google.gson.annotations.SerializedName

data class RawNews(

    @SerializedName("Type") val type : Int,
    @SerializedName("Message") val message : String,
//    @SerializedName("Promoted") val promoted : List<String>,
    @SerializedName("Data") val data : ArrayList<Data>,
//    @SerializedName("RateLimit") val rateLimit : RateLimit,
    @SerializedName("HasWarning") val hasWarning : Boolean
)