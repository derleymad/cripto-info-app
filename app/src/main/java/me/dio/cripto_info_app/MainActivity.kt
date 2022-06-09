package me.dio.cripto_info_app

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var resultBTC: TextView
    private lateinit var resultETH: TextView
    private lateinit var sinalBTC: TextView
    private lateinit var sinalETH: TextView
    private lateinit var converterBTC: TextView
    private lateinit var converterETH: TextView
    private lateinit var inputBTC: EditText
    private lateinit var inputETH: EditText
    private lateinit var btnBTC: Button
    private lateinit var btnETH: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        resultBTC = findViewById<TextView>(R.id.priceBTC)//LABEL
        resultETH = findViewById<TextView>(R.id.priceETH)//LABEL
        sinalBTC = findViewById<TextView>(R.id.sinalBTC)//LABEL
        sinalETH = findViewById<TextView>(R.id.sinalETH)//LABEL
        converterBTC = findViewById<TextView>(R.id.converterBTC) //LABEL
        converterETH = findViewById<TextView>(R.id.converterETH) //LABEL



        inputBTC = findViewById<EditText>(R.id.inputBTC) //INPUT BTC
        inputETH = findViewById<EditText>(R.id.inputETH) //INPUT ETH
        btnBTC = findViewById<Button>(R.id.btnBTC) //BUTTOM BTC
        btnETH = findViewById<Button>(R.id.btnETH) //BUTTOM ETH


        getBTCvalue()
        getETHvalue()

        btnBTC.setOnClickListener {
            calculateBTC()
        }
        btnETH.setOnClickListener {
            calculateETH()
        }
  }
    private fun getBTCvalue(){
        Thread{
            val urlBTC = URL("https://www.mercadobitcoin.net/api/BTC/ticker") //BTC URL
            val conBTC = urlBTC.openConnection() as HttpsURLConnection

            try {
                val dataBTC = conBTC.inputStream.bufferedReader().readText()
                val objBTC = JSONObject(dataBTC)

                val ticker = objBTC.getJSONObject("ticker")

                val resBTC = ticker.getDouble("last")
                val lowBTC = ticker.getDouble("low")
                val highBTC = ticker.getDouble("high")
                val sinal = highBTC.minus(lowBTC)
                val total = (sinal * 100) / highBTC

                runOnUiThread {
                    if (sinal>0){
                        //BTC ta subindo ++
                        sinalBTC.visibility = View.VISIBLE
                        sinalBTC.setTextColor(Color.parseColor("#00FF00"))
                        sinalBTC.text = "+ ${String.format("%.2f",total)}%"

                    }else{
                        //BTC ta caindo --
                        sinalBTC.visibility = View.VISIBLE
                        sinalBTC.setTextColor(Color.parseColor("#FF0000"))
                        sinalBTC.text = "${String.format("R$ %.2f",total)}%"
                    }

                    //resultBTC.text = "R$ ${resBTC.toString().format(2)} "
                    resultBTC.text = " ${String.format("%.2f",resBTC)}"
                }
            }finally {
                conBTC.disconnect()
            }
        }.start()
    }

    private fun getETHvalue(){

        Thread{
            val urlETH = URL("https://www.mercadobitcoin.net/api/ETH/ticker") //ETH URL
            val conETH = urlETH.openConnection() as HttpsURLConnection

            try{
                val dataETH = conETH.inputStream.bufferedReader().readText()
                val objETH = JSONObject(dataETH)

                val ticker = objETH.getJSONObject("ticker")

                val resETH = ticker.getDouble("last")
                val lowETH = ticker.getDouble("low")
                val highETH = ticker.getDouble("high")
                val sinal = highETH.minus(lowETH)
                val total = (sinal * 100) / highETH

                runOnUiThread { //ETH

                    val resETH = objETH.getJSONObject("ticker").getDouble("last")
                    resultETH.text = "${String.format("%.2f",resETH)}"

                    if (total>0){
                        sinalETH.visibility = View.VISIBLE
                        sinalETH.setTextColor(Color.parseColor("#00FF00"))
                        sinalETH.text = "+${String.format("%.2f",total)}%"
                    }else{
                        sinalETH.visibility = View.VISIBLE
                        sinalETH.setTextColor(Color.parseColor("#FF0000"))
                        sinalETH.text = "+${String.format("%.2f",total)}%"
                    }
                }
            }finally {
                conETH.disconnect()
            }
        }.start()
    }

    private fun calculateBTC(){
        val value = inputBTC.text.toString()
        if (value.isEmpty() || value.equals(".")){
            converterBTC.text = "Valor inválido!"
            converterBTC.visibility = View.VISIBLE
            return
        }
        Thread{
            val num = value.toDouble()
            val total = num.div(resultBTC.text.toString().replace(",",".").toDouble())
            runOnUiThread {
                converterBTC.text = "${String.format("%.4f",total)}"
                converterBTC.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun calculateETH() {
        val value = inputETH.text.toString()
        if (value.isEmpty() || value.equals(".")){
            converterETH.text = "Valor inválido!"
            converterETH.visibility = View.VISIBLE
            return
        }
        Thread{
            val num = value.toDouble()
            val total = num.div(resultETH.text.toString().replace(",",".").toDouble())
            runOnUiThread {
                converterETH.text = "${String.format("%.4f",total)}"
                converterETH.visibility = View.VISIBLE
            }
        }.start()
        }

}
