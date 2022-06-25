package com.dkaera.mash

import android.app.Application
import timber.log.Timber

class MashApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        Timber.plant(Timber.DebugTree())
    }
}