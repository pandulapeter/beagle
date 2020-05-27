package com.pandulapeter.beagle.implementation

import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagleCore.implementation.manager.UiManagerContract

internal class BottomSheetUiManager : UiManagerContract {

    override fun show(activity: FragmentActivity) = true.also { BeagleBottomSheet.show(activity.supportFragmentManager) }

    override fun hide(activity: FragmentActivity) = (activity.supportFragmentManager.findFragmentByTag(BeagleBottomSheet.TAG) as? BeagleBottomSheet?)?.let {
        it.dismiss()
        true
    } ?: false
}