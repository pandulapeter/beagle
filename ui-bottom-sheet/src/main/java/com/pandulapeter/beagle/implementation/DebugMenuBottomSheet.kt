package com.pandulapeter.beagle.implementation

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.DebugMenuView
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.core.util.extension.applyTheme
import kotlin.math.min
import kotlin.math.roundToInt

//TODO: The status bar is not properly handled.
internal class DebugMenuBottomSheet : BottomSheetDialogFragment(), UpdateListener {

    private var slideOffset = 0f
    private val topInset get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) dialog?.window?.decorView?.rootWindowInsets?.systemWindowInsetTop ?: 0 else 0
    private val bottomInset get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) dialog?.window?.decorView?.rootWindowInsets?.systemWindowInsetBottom ?: 0 else 0
    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            this@DebugMenuBottomSheet.slideOffset = slideOffset
            updateApplyResetBlockPosition()
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) = updateApplyResetBlockPosition()
    }
    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var bottomSheetView: View

    override fun getContext() = super.getContext()?.applyTheme()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = DebugMenuView(requireContext())

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState).also {
        slideOffset = savedInstanceState?.getFloat(SLIDE_OFFSET, slideOffset) ?: slideOffset
    }

    override fun onResume() {
        super.onResume()
        val displayMetrics = DisplayMetrics()
        BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        dialog?.findViewById<View>(R.id.design_bottom_sheet)?.run {
            bottomSheetView = this
            BottomSheetBehavior.from(this).run {
                behavior = this
                addBottomSheetCallback(bottomSheetCallback)
            }
            dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            dialog?.window?.decorView?.run {
                setOnApplyWindowInsetsListener { _, insets -> insets.also { updateSize(displayMetrics) } }
                requestApplyInsets()
            }
        } else {
            updateSize(displayMetrics)
        }
    }

    override fun onPause() {
        super.onPause()
        behavior.removeBottomSheetCallback(bottomSheetCallback)
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

    private fun updateSize(displayMetrics: DisplayMetrics) = bottomSheetView.run {
        layoutParams = layoutParams.apply {
            height = displayMetrics.heightPixels - topInset
            width = min(displayMetrics.widthPixels, resources.getDimensionPixelSize(R.dimen.beagle_bottom_sheet_maximum_width))
            behavior.peekHeight = height / 2
            post { updateApplyResetBlockPosition() }
        }
    }

    private fun updateApplyResetBlockPosition() {
        view?.let { debugMenu ->
            (debugMenu as ViewGroup).getChildAt(1).run {
                layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    bottomMargin = debugMenu.height / 2 - bottomInset - ((debugMenu.height - bottomInset * 2) * slideOffset * 0.5f).roundToInt()
                }
            }
        }
    }

    companion object {
        const val TAG = "debugMenuBottomSheet"
        private const val SLIDE_OFFSET = "slideOffset"

        fun show(fragmentManager: FragmentManager) = DebugMenuBottomSheet().show(fragmentManager, TAG)
    }
}