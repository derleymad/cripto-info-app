package com.github.derleymad.lizwallet.ui.home.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.github.derleymad.lizwallet.R

class WebViewActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val webView: WebView = findViewById(R.id.web_view)

        webView.settings.javaScriptEnabled = true

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val link = intent.getStringExtra(EXTRA_LINK)

        if (link != null) {
            webView.loadUrl(link)
        }

        webView.webViewClient = WebViewClient()
    }
    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return true
    }

    companion object {
        const val EXTRA_LINK = "extra_link"
    }
}