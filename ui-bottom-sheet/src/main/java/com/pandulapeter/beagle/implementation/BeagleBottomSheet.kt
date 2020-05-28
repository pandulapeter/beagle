package com.pandulapeter.beagle.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R


internal class BeagleBottomSheet : BottomSheetDialogFragment() {

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
        const val TAG = "BeagleBottomSheet"

        fun show(fragmentManager: FragmentManager) = BeagleBottomSheet().show(fragmentManager, TAG)
    }
}