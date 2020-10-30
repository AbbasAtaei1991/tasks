package com.ataei.abbas.karam.utils

import android.content.Context
import android.graphics.Typeface

class TypefaceUtil {
    fun overrideFonts(context: Context, defaultFontToOverride:String, customFontFileNameInAssets:String){
        try {
            val customTypeface = Typeface.createFromAsset(context.assets,customFontFileNameInAssets)
            val defaultTypefaceField = Typeface::class.java.getDeclaredField(defaultFontToOverride)
            defaultTypefaceField.isAccessible = true
            defaultTypefaceField.set(null,customTypeface)
        }catch (e:Exception){
//            Timber.e("Cannot set font $customFontFileNameInAssets instead of $defaultFontToOverride")
        }
    }
}