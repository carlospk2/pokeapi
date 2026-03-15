package com.example.kmm_jcmm_test

import android.app.Application
import com.example.kmm_jcmm_test.di.viewModelModules
import com.example.kmm_jcmm_test.shared.di.initKoin
import org.koin.android.ext.koin.androidContext

class KmmApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(extraModules = viewModelModules) {
            androidContext(this@KmmApplication)
        }
    }
}
