package com.pandulapeter.beagle.logCrash.implementation

import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.getBeagleInsets
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.view.InternalDebugMenuView
import kotlin.math.roundToInt

internal class DebugMenuDialog : AppCompatDialogFragment() {

    override fun getContext() = super.getContext()?.applyTheme()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = InternalDebugMenuView(requireContext()).also {
        if (savedInstanceState == null) {
            BeagleCore.implementation.notifyVisibilityListenersOnShow()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.run {
            val displayMetrics = DisplayMetrics()
            BeagleCore.implementation.currentActivity?.run {
                windowManager.defaultDisplay.getMetrics(displayMetrics)
                val output =
                    window?.decorView?.let { view ->
                        view.rootWindowInsets?.let { insets ->
                            BeagleCore.implementation.appearance.applyInsets?.invoke(
                                WindowInsetsCompat.toWindowInsetsCompat(insets, view).getBeagleInsets(WindowInsetsCompat.Type.systemBars())
                            )
                        }
                    }
                val verticalInsets = output?.let { it.top + it.bottom }
                    ?: window.decorView.rootWindowInsets?.let {
                        val insets = WindowInsetsCompat.toWindowInsetsCompat(it, window.decorView).getBeagleInsets(WindowInsetsCompat.Type.systemBars())
                        insets.top + insets.bottom
                    } ?: 0
                val horizontalInsets = output?.let { it.left + it.right }
                    ?: window.decorView.rootWindowInsets?.let {
                        val insets = WindowInsetsCompat.toWindowInsetsCompat(it, window.decorView).getBeagleInsets(WindowInsetsCompat.Type.systemBars())
                        insets.left + insets.right
                    } ?: 0
                setLayout(
                    ((displayMetrics.widthPixels - horizontalInsets) * DIALOG_WIDTH_RATIO).roundToInt(),
                    ((displayMetrics.heightPixels - verticalInsets) * DIALOG_HEIGHT_RATIO).roundToInt()
                )
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!isStateSaved) {
            BeagleCore.implementation.notifyVisibilityListenersOnHide()
        }
    }

    companion object {
        private const val DIALOG_WIDTH_RATIO = 0.75f
        private const val DIALOG_HEIGHT_RATIO = 0.75f
        const val TAG = "debugMenuDialog"

        fun show(fragmentManager: FragmentManager) = DebugMenuDialog().show(fragmentManager, TAG)
    }
}