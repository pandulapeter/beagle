package com.pandulapeter.beagle.implementation

import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.core.manager.UiManagerContract

internal class DialogUiManager : UiManagerContract {

    override fun show(activity: FragmentActivity) =
        if (activity.supportFragmentManager.findFragmentByTag(BeagleDialog.TAG) as? BeagleDialog? == null) true.also { BeagleDialog.show(activity.supportFragmentManager) } else false

    override fun hide(activity: FragmentActivity) = (activity.supportFragmentManager.findFragmentByTag(BeagleDialog.TAG) as? BeagleDialog?)?.let {
        it.dismiss()
        true
    } ?: false
}