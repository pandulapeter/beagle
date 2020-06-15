package com.pandulapeter.beagle.implementation

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.DebugMenuView
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.core.util.extension.applyTheme

//TODO: The status bar is not properly handled.
internal class DebugMenuBottomSheet : BottomSheetDialogFragment() {

    override fun getContext() = super.getContext()?.applyTheme()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = DebugMenuView(requireContext())

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState).also { dialog ->
        dialog.setOnShowListener { dialogInterface ->
            (dialogInterface as BottomSheetDialog).findViewById<View>(R.id.design_bottom_sheet)?.run {
                layoutParams = layoutParams.apply {
                    val displayMetrics = DisplayMetrics()
                    BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
                    displayMetrics.heightPixels.let { windowHeight ->
                        if (windowHeight > 0) {
                            val insets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                dialog.window?.decorView?.rootWindowInsets?.let { it.systemWindowInsetTop + it.systemWindowInsetBottom } ?: 0
                            } else 0
                            height = windowHeight - insets
                            BottomSheetBehavior.from<View>(this@run).run {
                                peekHeight = windowHeight / 2
                            }
                        }
                    }
                    dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        BeagleCore.implementation.notifyVisibilityListenersOnShow()
    }

    override fun onStop() {
        super.onStop()
        BeagleCore.implementation.notifyVisibilityListenersOnHide()
    }

    companion object {
        const val TAG = "debugMenuBottomSheet"

        fun show(fragmentManager: FragmentManager) = DebugMenuBottomSheet().show(fragmentManager, TAG)
    }
}