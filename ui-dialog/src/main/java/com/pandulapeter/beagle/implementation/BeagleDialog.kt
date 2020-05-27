package com.pandulapeter.beagle.implementation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.Beagle


internal class BeagleDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = View(context).apply {
        setOnClickListener { Beagle.hide() }
        setBackgroundColor(Color.RED)
    }

    companion object {
        const val TAG = "BeagleDebugMenu"

        fun show(fragmentManager: FragmentManager) = BeagleDialog().show(fragmentManager, TAG)
    }
}