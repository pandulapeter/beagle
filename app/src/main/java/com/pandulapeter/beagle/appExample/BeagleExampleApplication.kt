package com.pandulapeter.beagle.appExample

import android.app.Application
import android.graphics.Color
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule

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
            Beagle.setModules(
                TextModule(
                    text = "This is a red text",
                    color = Color.RED
                ),
                TextModule(
                    text = "This is a blue text",
                    color = Color.BLUE
                ),
                TextModule(
                    text = "This text uses the default color"
                ),
                SwitchModule(
                    id = SHOULD_DRAW_CIRCLE_SWITCH_ID,
                    text = "Should draw circle",
                    shouldBePersisted = true,
                    onValueChanged = { Beagle.invalidateOverlay() }
                )
            )
        }
    }

    companion object {
        private const val SHOULD_DRAW_CIRCLE_SWITCH_ID = "shouldDrawCircle"
    }
}