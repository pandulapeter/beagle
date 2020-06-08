package com.pandulapeter.beagle.appDemo

import android.app.Application
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.TextModule

@Suppress("unused")
class BeagleDemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Beagle.initialize(
                application = this,
                appearance = Appearance(themeResourceId = R.style.BeagleTheme)
            )
            Beagle.setModules(
                LabelModule(title = "Welcome!"),
                TextModule(text = "Text...")
            )
        }
    }
}