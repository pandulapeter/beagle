package com.pandulapeter.beagle.implementation

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.core.manager.UiManagerContract

internal class ActivityUiManager : UiManagerContract {

    private val FragmentActivity.shouldShow get() = Beagle.isUiEnabled && this !is DebugMenuActivity
    private val FragmentActivity.shouldHide get() = Beagle.isUiEnabled && this is DebugMenuActivity

    override fun show(activity: FragmentActivity) = (activity.shouldShow).also { shouldShow ->
        if (shouldShow) {
            activity.startActivity(Intent(activity, DebugMenuActivity::class.java))
        }
    }

    override fun hide(activity: FragmentActivity) = (activity.shouldHide).also { shouldHide ->
        if (shouldHide) {
            activity.supportFinishAfterTransition()
        }
    }
}