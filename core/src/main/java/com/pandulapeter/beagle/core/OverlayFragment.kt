package com.pandulapeter.beagle.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pandulapeter.beagle.BeagleCore

internal class OverlayFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = BeagleCore.implementation.createOverlayLayout(requireActivity())

    companion object {
        const val TAG = "beagleOverlayFragment"

        fun newInstance() = OverlayFragment()
    }
}