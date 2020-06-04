package com.pandulapeter.beagle.core.manager

import android.content.Context
import android.view.View
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.pandulapeter.beagle.core.OverlayFragment
import com.pandulapeter.beagle.core.view.OverlayFrameLayout

@RestrictTo(RestrictTo.Scope.LIBRARY)
interface UiManagerContract {

    //TODO: Make sure this doesn't break the focus handling
    fun addOverlayFragment(activity: FragmentActivity) {
        activity.supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, findOverlayFragment(activity) ?: OverlayFragment.newInstance(), OverlayFragment.TAG)
            .commit()
    }

    fun createOverlayLayout(activity: FragmentActivity): View = OverlayFrameLayout(activity)

    fun findOverlayFragment(activity: FragmentActivity?): Fragment? = activity?.supportFragmentManager?.findFragmentByTag(OverlayFragment.TAG)

    fun findOverlayView(activity: FragmentActivity?): View? = findOverlayFragment(activity)?.view

    fun show(activity: FragmentActivity): Boolean = false

    fun hide(activity: FragmentActivity): Boolean = false
}