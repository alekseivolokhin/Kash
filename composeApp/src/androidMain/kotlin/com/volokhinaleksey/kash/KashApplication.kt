package com.volokhinaleksey.kash

import android.app.Application
import com.volokhinaleksey.kash.di.initKoin
import org.koin.android.ext.koin.androidContext

class KashApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@KashApplication)
        }
    }
}
