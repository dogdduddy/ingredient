package com.dogdduddy.ingredient.network

import android.app.Application
import android.content.Context
import com.dogdduddy.ingredient.R
import com.facebook.appevents.AppEventsLogger
import com.kakao.sdk.common.KakaoSdk

class App:Application() {
    companion object {
        var instance: App? = null
        var appContext : Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        instance = this

        AppEventsLogger.activateApp(this);

        // Kakao SDK 초기화
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        // var keyHash = Utility.getKeyHash(this)
    }
    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getAppContext() : App {
        checkNotNull(instance){
            "This Application does not inherit com.dogdduddy.App"
        }
        return instance!!
    }
}

