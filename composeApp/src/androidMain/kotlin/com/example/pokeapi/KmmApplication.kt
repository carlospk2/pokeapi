package com.example.pokeapi

import android.app.Application
import com.example.pokeapi.di.viewModelModules
import com.example.pokeapi.shared.di.initKoin
import org.koin.android.ext.koin.androidContext

class KmmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(extraModules = viewModelModules) {
            androidContext(this@KmmApplication)
        }
    }
}
