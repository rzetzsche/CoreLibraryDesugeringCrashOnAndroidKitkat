package com.plauzeware.corelibrarydesugeringcrashonandroidkitkat

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import java.util.*

class CoreApplication : MultiDexApplication() {
    override fun attachBaseContext(base: Context?) {
        //adding the new GMS causes to many Methods in APK, therefore configure Application as MultiDex
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Optional.of("TEst")
        Loader.load()
    }
}