package com.pandulapeter.beagle.core.manager

import androidx.annotation.RestrictTo
import androidx.fragment.app.FragmentActivity

@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface UiManagerContract {

    fun show(activity: FragmentActivity): Boolean = false

    fun hide(activity: FragmentActivity): Boolean = false
}