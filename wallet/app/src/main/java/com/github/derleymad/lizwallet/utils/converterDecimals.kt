package com.github.derleymad.lizwallet.utils
fun formatarNumero(cursoes: Double): String {
    val milhao = 1_000_000.0
    val bilhao = 1_000_000_000.0
    val trilhao = 1_000_000_000_000.0

    return when {
        cursoes >= trilhao -> String.format("%.2f T", cursoes / trilhao)
        cursoes >= bilhao -> String.format("%.2f B", cursoes / bilhao)
        cursoes >= milhao -> String.format("%.2f M", cursoes / milhao)
        else -> String.format("%.2f", cursoes)
    }
}