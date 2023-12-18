package com.github.derleymad.lizwallet.utils

import java.text.NumberFormat
import java.util.Locale


fun converSaldoToBeaty(number : Long) : String{
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    val formattedNumber = numberFormat.format(number)
    return formattedNumber
}