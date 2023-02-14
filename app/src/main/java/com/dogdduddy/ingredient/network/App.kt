package com.dogdduddy.ingredient.network

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

        /*
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

         */
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
            "This Application does not inherit com.dogdduddy.App"
        }
        return instance!!
    }
}

