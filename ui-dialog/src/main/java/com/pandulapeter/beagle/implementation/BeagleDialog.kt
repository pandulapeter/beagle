package com.pandulapeter.beagle.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleView


internal class BeagleDialog : AppCompatDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = BeagleView(requireContext()).apply {
        setOnClickListener { Beagle.hide() }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState).also { dialog ->
        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    companion object {
        const val TAG = "BeagleDialog"

        fun show(fragmentManager: FragmentManager) = BeagleDialog().show(fragmentManager, TAG)
    }
}