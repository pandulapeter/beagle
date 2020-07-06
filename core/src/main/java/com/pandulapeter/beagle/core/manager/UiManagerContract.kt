package com.pandulapeter.beagle.core.manager

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.core.OverlayFragment
import com.pandulapeter.beagle.core.view.OverlayFrameLayout

interface UiManagerContract {

    //TODO: Make sure this doesn't break the focus handling
    fun addOverlayFragment(activity: FragmentActivity) {
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, findOverlayFragment(activity) ?: OverlayFragment.newInstance(), OverlayFragment.TAG)
            .commit()
    }

    fun createOverlayLayout(activity: FragmentActivity): View = OverlayFrameLayout(activity)

    fun findOverlayFragment(activity: FragmentActivity?): Fragment? = activity?.supportFragmentManager?.findFragmentByTag(OverlayFragment.TAG)

    fun findHostFragmentManager(): FragmentManager? = null

    fun findOverlayView(activity: FragmentActivity?): View? = findOverlayFragment(activity)?.view

    fun show(activity: FragmentActivity): Boolean = false

    fun hide(activity: FragmentActivity?): Boolean = false
}