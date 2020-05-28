package com.pandulapeter.beagle.implementation

import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R


internal class BeagleActivity : AppCompatActivity(R.layout.activity_beagle) {

    override fun onStart() {
        super.onStart()
        BeagleCore.implementation.notifyVisibilityListenersOnShow()
    }

    override fun onStop() {
        super.onStop()
        BeagleCore.implementation.notifyVisibilityListenersOnHide()
    }
}