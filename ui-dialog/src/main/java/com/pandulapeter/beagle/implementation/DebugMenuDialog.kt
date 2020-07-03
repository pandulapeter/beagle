package com.pandulapeter.beagle.implementation

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.DebugMenuView
import com.pandulapeter.beagle.core.util.extension.applyTheme
import kotlin.math.roundToInt

internal class DebugMenuDialog : AppCompatDialogFragment() {

    override fun getContext() = super.getContext()?.applyTheme()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = DebugMenuView(context!!)

    override fun onResume() {
        super.onResume()
        view?.run {
            layoutParams = layoutParams.apply {
                val displayMetrics = DisplayMetrics()
                BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
                val verticalInsets =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) dialog?.window?.decorView?.rootWindowInsets?.let { it.systemWindowInsetTop + it.stableInsetBottom } ?: 0 else 0
                val horizontalInsets =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) dialog?.window?.decorView?.rootWindowInsets?.let { it.systemWindowInsetLeft + it.stableInsetRight } ?: 0 else 0
                width = ((displayMetrics.widthPixels - horizontalInsets) * DIALOG_WIDTH_RATIO).roundToInt()
                height = ((displayMetrics.heightPixels - verticalInsets) * DIALOG_HEIGHT_RATIO).roundToInt()
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
        private const val DIALOG_WIDTH_RATIO = 0.75f
        private const val DIALOG_HEIGHT_RATIO = 0.75f
        const val TAG = "debugMenuDialog"

        fun show(fragmentManager: FragmentManager) = DebugMenuDialog().show(fragmentManager, TAG)
    }
}