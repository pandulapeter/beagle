package com.pandulapeter.beagle.implementation

import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagleCore.implementation.manager.UiManagerContract

internal class DIalogUiManager : UiManagerContract {

    override fun show(activity: FragmentActivity) = true.also { BeagleDialog.show(activity.supportFragmentManager) }

    override fun hide(activity: FragmentActivity) = (activity.supportFragmentManager.findFragmentByTag(BeagleDialog.TAG) as? BeagleDialog?)?.let {
        it.dismiss()
        true
    } ?: false
}