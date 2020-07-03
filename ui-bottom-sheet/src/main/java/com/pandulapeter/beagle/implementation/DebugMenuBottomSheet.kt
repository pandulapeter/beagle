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
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.core.util.extension.applyTheme

//TODO: The status bar is not properly handled.
internal class DebugMenuBottomSheet : BottomSheetDialogFragment(), UpdateListener {

    private var slideOffset = 0f
    private val topInset get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) dialog?.window?.decorView?.rootWindowInsets?.systemWindowInsetTop ?: 0 else 0

    override fun getContext() = super.getContext()?.applyTheme()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = DebugMenuView(requireContext())

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState).also { dialog ->
        slideOffset = savedInstanceState?.getFloat(SLIDE_OFFSET, slideOffset) ?: slideOffset
        dialog.setOnShowListener { dialogInterface ->
            (dialogInterface as BottomSheetDialog).findViewById<View>(R.id.design_bottom_sheet)?.run {
                layoutParams = layoutParams.apply {
                    val displayMetrics = DisplayMetrics()
                    BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
                    displayMetrics.heightPixels.let { windowHeight ->
                        if (windowHeight > 0) {
                            val insets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                dialog.window?.decorView?.rootWindowInsets?.systemWindowInsetTop ?: 0
                            } else 0
                            height = windowHeight - insets
                            BottomSheetBehavior.from<View>(this@run).run {
                                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                                        this@DebugMenuBottomSheet.slideOffset = slideOffset
                                        updateApplyResetBlockPosition()
                                    }

                                    override fun onStateChanged(bottomSheet: View, newState: Int) = updateApplyResetBlockPosition()
                                })
                                peekHeight = windowHeight / 2
                            }
                        }
                    }
                    dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    post { updateApplyResetBlockPosition() }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(SLIDE_OFFSET, slideOffset)
    }

    override fun onStart() {
        super.onStart()
        BeagleCore.implementation.notifyVisibilityListenersOnShow()
        BeagleCore.implementation.addInternalUpdateListener(this)
    }

    override fun onStop() {
        BeagleCore.implementation.removeUpdateListener(this)
        BeagleCore.implementation.notifyVisibilityListenersOnHide()
        super.onStop()
    }

    override fun onContentsChanged() = updateApplyResetBlockPosition()

    private fun updateApplyResetBlockPosition() {
        view?.let { view ->
            (view as ViewGroup).getChildAt(1).run {
                y = (view.height - topInset) * slideOffset * 0.5f - height + (view.height + topInset) / 2
            }
        }
    }

    companion object {
        const val TAG = "debugMenuBottomSheet"
        private const val SLIDE_OFFSET = "slideOffset"

        fun show(fragmentManager: FragmentManager) = DebugMenuBottomSheet().show(fragmentManager, TAG)
    }
}