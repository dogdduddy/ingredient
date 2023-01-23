package com.example.ingredient.global

import android.app.Application
import android.content.Context
import com.facebook.appevents.AppEventsLogger
import com.kakao.auth.KakaoSDK

class App:Application() {
    companion object {
        var instance: App? = null
        var appContext : Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        instance = this

        if(KakaoSDK.getAdapter() == null) {
            KakaoSDK.init(KakaoSDKAdapter(getAppContext()))
        }
        AppEventsLogger.activateApp(this);

    }
    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getAppContext() : App {
        checkNotNull(instance){
            "This Application does not inherit com.example.App"
        }
        return instance!!
    }
}

