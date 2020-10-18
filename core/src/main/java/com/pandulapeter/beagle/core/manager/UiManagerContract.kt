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

    fun addOverlayFragment(activity: FragmentActivity) {
        val overlayFragment = findOverlayFragment(activity) ?: OverlayFragment.newInstance()
        activity.supportFragmentManager
            .beginTransaction()
            .apply {
                if (overlayFragment.isAdded) {
                    show(overlayFragment)
                } else {
                    add(android.R.id.content, overlayFragment, OverlayFragment.TAG)
                }
            }
            .setReorderingAllowed(true)
            .commit()
    }

    fun createOverlayLayout(activity: FragmentActivity): View = OverlayFrameLayout(activity)

    fun findOverlayFragment(activity: FragmentActivity?): Fragment? = activity?.supportFragmentManager?.findFragmentByTag(OverlayFragment.TAG)

    fun findHostFragmentManager(): FragmentManager? = null

    fun findOverlayView(activity: FragmentActivity?): View? = findOverlayFragment(activity)?.view

    fun show(activity: FragmentActivity): Boolean = false

    fun hide(activity: FragmentActivity?): Boolean = false

    fun isActivityDebugMenu(activity: Activity) = activity is GalleryActivity || activity is BugReportActivity

    fun isFragmentDebugMenu(fragment: Fragment) = false
}