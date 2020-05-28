package com.pandulapeter.beagle.core.manager

import androidx.annotation.RestrictTo
import androidx.fragment.app.FragmentActivity

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface UiManagerContract {

    fun onActivityCreated(activity: FragmentActivity) = Unit

    fun onActivityDestroyed(activity: FragmentActivity) = Unit

    fun show(activity: FragmentActivity): Boolean = false

    fun hide(activity: FragmentActivity): Boolean = false
}