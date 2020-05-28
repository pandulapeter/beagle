package com.pandulapeter.beagle.appExample

import android.app.Application
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.shared.configuration.Appearance

@Suppress("unused")
class BeagleExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Beagle.initialize(
                application = this,
                appearance = Appearance(
                    themeResourceId = R.style.BeagleTheme
                )
            )
        }
    }
}