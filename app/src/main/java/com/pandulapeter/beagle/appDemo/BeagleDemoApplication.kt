package com.pandulapeter.beagle.appDemo

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.common.configuration.Appearance
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class BeagleDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BeagleDemoApplication)
            modules(modules)
        }
        Beagle.initialize(this, Appearance(themeResourceId = R.style.DebugMenuTheme))
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        @Suppress("ConstantConditionIf")
        if (BuildConfig.BUILD_TYPE == "debug") {
            MultiDex.install(this)
        }
    }
}