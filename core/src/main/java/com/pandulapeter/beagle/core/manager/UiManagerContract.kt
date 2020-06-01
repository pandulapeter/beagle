package com.pandulapeter.beagle.core.manager

import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.findRootViewGroup
import com.pandulapeter.beagle.core.view.OverlayFrameLayout

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface UiManagerContract {

    //TODO: Make sure this doesn't break Activity shared element transitions.
    //TODO: Make sure this doesn't break the focus handling
    fun injectOverlayFrameLayout(activity: FragmentActivity, oldRootViewGroup: ViewGroup, overlayFrameLayout: OverlayFrameLayout) {
        oldRootViewGroup.run {
            post {
                val oldViews = (0 until childCount).map { getChildAt(it) }
                removeAllViews()
                oldViews.forEach { overlayFrameLayout.addView(it) }
                addView(overlayFrameLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        }
    }

    fun onActivityDestroyed(activity: FragmentActivity) = Unit

    fun invalidateOverlay() {
        BeagleCore.implementation.currentActivity?.findRootViewGroup()?.getChildAt(0)?.postInvalidate()
    }

    fun show(activity: FragmentActivity): Boolean = false

    fun hide(activity: FragmentActivity): Boolean = false
}