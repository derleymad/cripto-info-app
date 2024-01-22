package com.github.derleymad.lizwallet.model

data class HistoricalData(
    val prices : List<List<Float>>,
    val market_caps : List<List<Float>>,
    val total_volumes : List<List<Float>>
)