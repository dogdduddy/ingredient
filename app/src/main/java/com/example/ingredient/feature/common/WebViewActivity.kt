package com.example.ingredient.feature.common

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.ingredient.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
	private lateinit var binding: ActivityWebViewBinding
	private lateinit var webView:WebView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityWebViewBinding.inflate(layoutInflater)
		setContentView(binding.root)

		webView = binding.webView

		var webSettings = webView.settings
		webSettings.javaScriptEnabled = true // allow the js

		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // 화면이 계속 켜짐
		requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_USER

		webView.webViewClient = WebViewClient()

		webView.loadUrl(intent.extras!!.get("link").toString())

		/*
		var actionBar:ActionBar = supportActionBar!!
		actionBar.hide()

		 */
	}
}