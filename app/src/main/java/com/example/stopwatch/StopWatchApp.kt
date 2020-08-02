package com.example.stopwatch

import android.app.Application
import com.example.di.DependenciesModuleHome
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StopWatchApp: Application() {

        companion object{
            lateinit var mApp: StopWatchApp
                private set
        }
        override fun onCreate(){
            super.onCreate()
            mApp = this
            startKoin {
                androidContext(this@StopWatchApp)
                modules(listOf(DependenciesModuleHome))
            }
        }

    }