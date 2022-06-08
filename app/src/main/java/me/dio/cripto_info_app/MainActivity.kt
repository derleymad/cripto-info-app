package me.dio.cripto_info_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import android.widget.EditText
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var resultBTC: TextView
    private lateinit var resultETH: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        resultBTC = findViewById<TextView>(R.id.priceBTC)
        resultETH = findViewById<TextView>(R.id.priceETH)
        getBTCvalue()
        getETHvalue()
  }
    private fun getBTCvalue(){
        Thread{
            val urlBTC = URL("https://www.mercadobitcoin.net/api/BTC/ticker") //BTC URL
            val conBTC = urlBTC.openConnection() as HttpsURLConnection

            try {
                val dataBTC = conBTC.inputStream.bufferedReader().readText()
                val objBTC = JSONObject(dataBTC)
                runOnUiThread {
                    val resBTC = objBTC.getJSONObject("ticker").getDouble("last")
                    //resultBTC.text = "R$ ${resBTC.toString().format(2)} "
                    resultBTC.text = "R$ ${resBTC.toString().format(2)}"
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

                runOnUiThread { //for BTC and ETH

                    val resETH = objETH.getJSONObject("ticker").getDouble("last")
                    resultETH.text = "R$ ${resETH.toString().format(2)}"
                }
            }finally {
                conETH.disconnect()
            }
        }.start()

    }
}
