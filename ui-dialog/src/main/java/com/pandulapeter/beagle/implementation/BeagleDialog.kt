package com.pandulapeter.beagle.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R


internal class BeagleDialog : AppCompatDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.view_beagle, container)

    override fun onStart() {
        super.onStart()
        BeagleCore.implementation.notifyVisibilityListenersOnShow()
    }

    override fun onStop() {
        super.onStop()
        BeagleCore.implementation.notifyVisibilityListenersOnHide()
    }

    companion object {
        const val TAG = "BeagleDialog"

        fun show(fragmentManager: FragmentManager) = BeagleDialog().show(fragmentManager, TAG)
    }
}