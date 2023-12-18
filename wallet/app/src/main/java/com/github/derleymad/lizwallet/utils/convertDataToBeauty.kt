package com.github.derleymad.lizwallet.utils

import java.util.Date



fun converDataToBeaty(data: Long) : String{

    val timestampPublicacao = data
    val dataPublicacao = Date(timestampPublicacao * 1000);

    val dataAtual = Date();
    val diferencaEmMillis = dataAtual.getTime() - dataPublicacao.getTime();

    val segundos = diferencaEmMillis / 1000;
    val minutos = segundos / 60;
    val horas = minutos / 60;
    val dias = horas / 24;
    var formattedDate =""

    return if (dias > 0) {
        formattedDate = ("Há $dias dias");
        formattedDate
    } else if (horas > 0) {
        formattedDate = ("Há $horas horas");
        formattedDate
    } else if (minutos > 0) {
        formattedDate = ("Há $minutos minutos");
        formattedDate
    } else {
        formattedDate = ("Há $segundos segundos");
        formattedDate
    }
}
