package com.ataei.abbas.karam

import android.app.Application
import com.ataei.abbas.karam.utils.TypefaceUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        TypefaceUtil().overrideFonts(applicationContext, "SERIF", "fonts/iran_sans.ttf")
    }
}