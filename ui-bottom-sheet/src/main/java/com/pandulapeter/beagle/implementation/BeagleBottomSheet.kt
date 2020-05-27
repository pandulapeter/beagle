package com.pandulapeter.beagle.implementation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pandulapeter.beagle.Beagle


internal class BeagleBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = View(context).apply {
        setOnClickListener { Beagle.hide() }
        setBackgroundColor(Color.RED)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState).also { dialog ->
        dialog.window?.let { window ->
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }

    companion object {
        const val TAG = "BeagleDebugMenu"

        fun show(fragmentManager: FragmentManager) = BeagleBottomSheet().show(fragmentManager, TAG)
    }
}