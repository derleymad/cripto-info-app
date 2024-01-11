package com.github.derleymad.lizwallet.model.news

import Source_info
import com.google.gson.annotations.SerializedName

data class Data (

    val id : Int,
    val guid : String,
    val published_on : Int,
    val imageurl : String,
    var title : String,
    val url : String,
    var body : String,
    val tags : String,
    val lang : String,
    val upvotes : Int,
    val downvotes : Int,
    val categories : String,
    val source_info : Source_info,
    val source : String
)