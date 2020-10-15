package com.pandulapeter.beagle.implementation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.core.manager.UiManagerContract

internal class BottomSheetUiManager : UiManagerContract {

    private val FragmentActivity.shouldShow get() = Beagle.isUiEnabled && supportFragmentManager.findFragmentByTag(DebugMenuBottomSheet.TAG) as? DebugMenuBottomSheet? == null
    private val FragmentActivity.dialogToHide get() = if (Beagle.isUiEnabled) supportFragmentManager.findFragmentByTag(DebugMenuBottomSheet.TAG) as? DebugMenuBottomSheet? else null

    override fun show(activity: FragmentActivity) = (activity.shouldShow).also { shouldShow ->
        if (shouldShow) {
            DebugMenuBottomSheet.show(activity.supportFragmentManager)
        }
    }

    override fun hide(activity: FragmentActivity?) = (activity?.dialogToHide)?.let {
        it.dismiss()
        true
    } ?: false

    override fun isFragmentDebugMenu(fragment: Fragment) = fragment is DebugMenuBottomSheet
}