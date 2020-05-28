package com.pandulapeter.beagle.implementation

import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R

//TODO: The way visibility listeners are notified is buggy during configuration changes
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