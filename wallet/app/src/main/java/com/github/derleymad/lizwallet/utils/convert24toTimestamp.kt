package com.github.derleymad.lizwallet.utils

import java.util.Calendar
import java.util.Date

private fun convertToTimestamp(date: Date): Long {
    return date.time / 1000
}

fun convert24toTimestamp() : Long {
    val currentTimeInMillis = System.currentTimeMillis()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = currentTimeInMillis
    calendar.add(Calendar.HOUR_OF_DAY, -24)
    val twentyFourHoursAgo = calendar.time

    // Converte a data para timestamp Unix
    val timestamp = convertToTimestamp(twentyFourHoursAgo)
    return timestamp
}