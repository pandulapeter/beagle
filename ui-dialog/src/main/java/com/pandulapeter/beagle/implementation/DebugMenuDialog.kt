package com.pandulapeter.beagle.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.DebugMenuViewView

internal class DebugMenuDialog : AppCompatDialogFragment() {

    override fun getContext() = super.getContext()?.let { BeagleCore.implementation.getThemedContext(it) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = DebugMenuViewView(context!!)

    override fun onStart() {
        super.onStart()
        BeagleCore.implementation.notifyVisibilityListenersOnShow()
    }

    override fun onStop() {
        super.onStop()
        BeagleCore.implementation.notifyVisibilityListenersOnHide()
    }

    companion object {
        const val TAG = "DebugMenuDialog"

        fun show(fragmentManager: FragmentManager) = DebugMenuDialog().show(fragmentManager, TAG)
    }
}