package com.pandulapeter.beagle.implementation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pandulapeter.beagle.Beagle

internal class BeagleDebugMenu : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = View(context).apply {
        setOnClickListener { Beagle.hide() }
    }

    companion object {
        const val TAG = "BeagleDebugMenu"

        fun show(fragmentManager: FragmentManager) = BeagleDebugMenu().show(fragmentManager, TAG)
    }
}