package com.github.derleymad.lizwallet.utils

import java.text.NumberFormat
import java.util.Locale


fun converSaldoToBeaty(number : String) : String{
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    val formattedNumber = numberFormat.format(number.toFloat())
    return formattedNumber
}