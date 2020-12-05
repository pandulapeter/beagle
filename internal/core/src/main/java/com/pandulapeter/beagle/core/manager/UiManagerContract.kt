package com.pandulapeter.beagle.core.manager

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.core.OverlayFragment
import com.pandulapeter.beagle.core.view.OverlayFrameLayout
import com.pandulapeter.beagle.core.view.bugReport.BugReportActivity
import com.pandulapeter.beagle.core.view.gallery.GalleryActivity

interface UiManagerContract {

    fun addOverlayFragment(currentFragment: Fragment?, activity: FragmentActivity) {
        if (currentFragment !is OverlayFragment) {
            val hasRootFragmentChanged = currentFragment?.id == android.R.id.content
            if (hasRootFragmentChanged) {
                activity.supportFragmentManager
                    .beginTransaction()
                    .apply {
                        activity.supportFragmentManager.fragments.forEach {
                            if (it is OverlayFragment) {
                                remove(it)
                            }
                        }
                    }
                    .setReorderingAllowed(true)
                    .runOnCommit {
                        activity.supportFragmentManager
                            .beginTransaction()
                            .add(android.R.id.content, OverlayFragment.newInstance())
                            .setReorderingAllowed(true)
                            .commitAllowingStateLoss()
                    }
                    .commitAllowingStateLoss()
            } else {
                if (activity.supportFragmentManager.fragments.lastOrNull() !is OverlayFragment) {
                    activity.supportFragmentManager
                        .beginTransaction()
                        .run {
                            findOverlayFragment(activity)?.let(::remove)
                            add(android.R.id.content, OverlayFragment.newInstance())
                        }
                        .setReorderingAllowed(true)
                        .commitAllowingStateLoss()
                }
            }
        }
    }

    fun createOverlayLayout(activity: FragmentActivity): View = OverlayFrameLayout(activity)

    fun findOverlayFragment(activity: FragmentActivity?): Fragment? = activity?.supportFragmentManager?.findFragmentById(android.R.id.content) as? OverlayFragment?

    fun findHostFragmentManager(): FragmentManager? = null

    fun findOverlayView(activity: FragmentActivity?): View? = findOverlayFragment(activity)?.view

    fun show(activity: FragmentActivity): Boolean = false

    fun hide(activity: FragmentActivity?): Boolean = false

    fun isActivityDebugMenu(activity: Activity) = activity is GalleryActivity || activity is BugReportActivity

    fun isFragmentDebugMenu(fragment: Fragment) = false
}