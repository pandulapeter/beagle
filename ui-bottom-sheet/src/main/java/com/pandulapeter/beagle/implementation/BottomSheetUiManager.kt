package com.pandulapeter.beagle.implementation

import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.core.manager.UiManagerContract

internal class BottomSheetUiManager : UiManagerContract {

    private val FragmentActivity.shouldShow get() = Beagle.isUiEnabled && supportFragmentManager.findFragmentByTag(BeagleBottomSheet.TAG) as? BeagleBottomSheet? == null
    private val FragmentActivity.dialogToHide get() = if (Beagle.isUiEnabled) supportFragmentManager.findFragmentByTag(BeagleBottomSheet.TAG) as? BeagleBottomSheet? else null

    override fun show(activity: FragmentActivity) = (activity.shouldShow).also { shouldShow ->
        if (shouldShow) {
            BeagleBottomSheet.show(activity.supportFragmentManager)
        }
    }

    override fun hide(activity: FragmentActivity) = (activity.dialogToHide)?.let {
        it.dismiss()
        true
    } ?: false
}