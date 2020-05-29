package com.pandulapeter.beagle.implementation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.DebugMenuViewView

internal class DebugMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(DebugMenuViewView(this))
    }

    override fun onStart() {
        super.onStart()
        BeagleCore.implementation.notifyVisibilityListenersOnShow()
    }

    override fun onStop() {
        super.onStop()
        BeagleCore.implementation.notifyVisibilityListenersOnHide()
    }
}