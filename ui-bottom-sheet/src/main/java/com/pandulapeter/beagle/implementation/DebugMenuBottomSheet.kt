package com.pandulapeter.beagle.implementation

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.common.configuration.Insets
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.view.InternalDebugMenuView
import kotlin.math.min
import kotlin.math.roundToInt

internal class DebugMenuBottomSheet : BottomSheetDialogFragment(), UpdateListener {

    private var slideOffset = 0f
    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            this@DebugMenuBottomSheet.slideOffset = slideOffset
            updateApplyResetBlockPosition()
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) = updateApplyResetBlockPosition()
    }
    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var bottomSheetView: View
    private val topInset
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Beagle.currentActivity?.window?.decorView?.rootWindowInsets?.let {
            BeagleCore.implementation.appearance.applyInsets?.invoke(
                Insets(
                    left = it.systemWindowInsetLeft,
                    top = it.systemWindowInsetTop,
                    right = it.systemWindowInsetRight,
                    bottom = it.systemWindowInsetBottom
                )
            )?.also { outputInsets ->
                (view as? InternalDebugMenuView)?.applyInsets(outputInsets.left, 0, outputInsets.right, outputInsets.bottom)
            }?.top ?: 0
        } ?: 0 else 0

    override fun getContext() = super.getContext()?.applyTheme()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = InternalDebugMenuView(requireContext())
        .also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Beagle.currentActivity?.window?.decorView?.run {
                    setOnApplyWindowInsetsListener { _, insets -> onApplyWindowInsets(insets).also { updateSize() } }
                    requestApplyInsets()
                }
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?) = super.onCreateDialog(savedInstanceState).also {
        slideOffset = savedInstanceState?.getFloat(SLIDE_OFFSET, slideOffset) ?: slideOffset
        it.setOnShowListener { updateSize() }
        if (savedInstanceState == null) {
            BeagleCore.implementation.notifyVisibilityListenersOnShow()
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.findViewById<View>(R.id.design_bottom_sheet)?.run {
            bottomSheetView = this
            BottomSheetBehavior.from(this).run {
                behavior = this
                addBottomSheetCallback(bottomSheetCallback)
            }
            dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
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
        BeagleCore.implementation.addInternalUpdateListener(this)
    }

    override fun onStop() {
        BeagleCore.implementation.removeUpdateListener(this)
        super.onStop()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!isStateSaved) {
            BeagleCore.implementation.notifyVisibilityListenersOnHide()
        }
    }

    override fun onContentsChanged() = updateApplyResetBlockPosition()

    private fun updateSize() = bottomSheetView.run {
        val displayMetrics = DisplayMetrics()
        BeagleCore.implementation.currentActivity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        layoutParams = layoutParams.apply {
            height = displayMetrics.heightPixels - topInset
            width = min(displayMetrics.widthPixels, resources.getDimensionPixelSize(R.dimen.beagle_bottom_sheet_maximum_width))
            behavior.peekHeight = height / 2
            post { updateApplyResetBlockPosition() }
        }
    }

    //TODO: Needs to be improved, it's glitchy
    private fun updateApplyResetBlockPosition() {
        view?.let { debugMenu ->
            (debugMenu as ViewGroup).getChildAt(1).run {
                layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                    bottomMargin = ((behavior.peekHeight) * (1 - slideOffset)).roundToInt()
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