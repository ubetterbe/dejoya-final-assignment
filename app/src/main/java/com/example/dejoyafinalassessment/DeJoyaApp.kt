package com.example.dejoyafinalassessment

import android.app.Application
import com.example.dejoyafinalassessment.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

// koin needs to start somewhere before any activity asks it for dependencies,
// so this runs first, before MainActivity or anything else
class DeJoyaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@DeJoyaApp)
            modules(appModule)
        }
    }
}
