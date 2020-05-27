package com.pandulapeter.beagle.implementation

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.core.implementation.manager.UiManagerContract

internal class ActivityUiManager : UiManagerContract {

    override fun show(activity: FragmentActivity) = if (activity is BeagleActivity) false else true.also {
        activity.startActivity(Intent(activity, BeagleActivity::class.java))
    }

    override fun hide(activity: FragmentActivity) = if (activity is BeagleActivity) true.also { activity.supportFinishAfterTransition() } else false
}