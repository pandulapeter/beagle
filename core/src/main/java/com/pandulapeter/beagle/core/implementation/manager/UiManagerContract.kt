package com.pandulapeter.beagle.core.implementation.manager

import androidx.annotation.RestrictTo
import androidx.fragment.app.FragmentActivity

/**
 * TODO: Create product flavors that implement this interface differently:
 *  - dialog
 *  - drawer
 *  - view
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
interface UiManagerContract {

    fun show(activity: FragmentActivity): Boolean

    fun hide(activity: FragmentActivity): Boolean
}