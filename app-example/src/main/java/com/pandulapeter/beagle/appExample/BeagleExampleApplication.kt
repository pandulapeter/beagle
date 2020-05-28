package com.pandulapeter.beagle.appExample

import android.app.Application
import android.widget.Toast
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.shared.configuration.Appearance
import com.pandulapeter.beagle.shared.contracts.VisibilityListener

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
            Beagle.addVisibilityListener(object : VisibilityListener {

                override fun onShown() = Toast.makeText(this@BeagleExampleApplication, "Debug menu shown", Toast.LENGTH_SHORT).show()

                override fun onHidden() = Toast.makeText(this@BeagleExampleApplication, "Debug menu hidden", Toast.LENGTH_SHORT).show()
            })
        }
    }
}