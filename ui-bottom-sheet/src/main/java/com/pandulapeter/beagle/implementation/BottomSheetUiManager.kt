package com.pandulapeter.beagle.implementation

import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.core.manager.UiManagerContract

internal class BottomSheetUiManager : UiManagerContract {

    override fun show(activity: FragmentActivity) =
        if (activity.supportFragmentManager.findFragmentByTag(BeagleBottomSheet.TAG) as? BeagleBottomSheet? == null) true.also { BeagleBottomSheet.show(activity.supportFragmentManager) } else false

    override fun hide(activity: FragmentActivity) = (activity.supportFragmentManager.findFragmentByTag(BeagleBottomSheet.TAG) as? BeagleBottomSheet?)?.let {
        it.dismiss()
        true
    } ?: false
}