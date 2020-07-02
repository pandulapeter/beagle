package com.pandulapeter.beagle

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.colorResource
import com.pandulapeter.beagle.core.util.extension.dimension
import com.pandulapeter.beagle.core.util.extension.drawable

//TODO: in the ui-view module should notify the listeners when this view is attached / detached
class DebugMenuView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context.applyTheme(), attrs, defStyleAttr), UpdateListener {

    private val verticalMargin = context.dimension(R.dimen.beagle_item_vertical_margin)
    private val recyclerView = RecyclerView(context.applyTheme(), attrs, defStyleAttr).apply {
        overScrollMode = View.OVER_SCROLL_NEVER
        clipToPadding = false
        setPadding(0, verticalMargin, 0, verticalMargin)
        BeagleCore.implementation.setupRecyclerView(this)
        minimumWidth = context.dimension(R.dimen.beagle_minimum_size)
        minimumHeight = context.dimension(R.dimen.beagle_minimum_size)
    }

    //TODO: Create a custom view for the button container and all related logic and improve their appearance
    private val largePadding = context.dimension(R.dimen.beagle_large_content_padding)
    private val applyButton = Button(context.applyTheme(), attrs, androidx.appcompat.R.attr.buttonStyle).apply {
        text = BeagleCore.implementation.appearance.applyButtonText
        setPadding(largePadding, largePadding, largePadding, largePadding)
        setOnClickListener { BeagleCore.implementation.applyPendingChanges() }
    }
    private val resetButton = Button(context.applyTheme(), attrs, androidx.appcompat.R.attr.buttonStyle).apply {
        text = BeagleCore.implementation.appearance.resetButtonText
        setPadding(largePadding, largePadding, largePadding, largePadding)
        setOnClickListener { BeagleCore.implementation.resetPendingChanges() }
    }
    private val buttonContainer = LinearLayout(context, attrs, defStyleAttr).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        addView(applyButton, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            leftMargin = largePadding
            rightMargin = largePadding / 2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                marginStart = leftMargin
                marginEnd = rightMargin
            }
        })
        addView(resetButton, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            leftMargin = largePadding / 2
            rightMargin = largePadding
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                marginStart = leftMargin
                marginEnd = rightMargin
            }
        })
        background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(Color.TRANSPARENT, context.applyTheme().colorResource(android.R.attr.textColorPrimary)))
    }

    init {
        setBackgroundFromWindowBackground()
        addView(recyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(buttonContainer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply { gravity = Gravity.BOTTOM })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            setOnApplyWindowInsetsListener { _, insets -> onApplyWindowInsets(insets) }
            requestApplyInsets()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        BeagleCore.implementation.addInternalUpdateListener(this)
    }

    override fun onDetachedFromWindow() {
        BeagleCore.implementation.removeUpdateListener(this)
        super.onDetachedFromWindow()
    }

    override fun onContentsChanged() {
        val hasPendingChanges = BeagleCore.implementation.hasPendingUpdates
        buttonContainer.run {
            post {
                animate()
                    .translationY(if (hasPendingChanges) 0f else height.toFloat())
                    .alpha(if (hasPendingChanges) 1f else 0f)
                    .start()
            }
        }
    }

    //TODO: Needs to be improved
    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            insets?.let {
                recyclerView.setPadding(it.systemWindowInsetLeft, it.systemWindowInsetTop + verticalMargin, it.systemWindowInsetRight, it.systemWindowInsetBottom + verticalMargin)
                buttonContainer.run { setPadding(paddingLeft, paddingTop, paddingRight, largePadding + insets.systemWindowInsetBottom) }
            }
        }
        return super.onApplyWindowInsets(insets)
    }

    private fun setBackgroundFromWindowBackground() {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            setBackgroundColor(typedValue.data)
        } else {
            background = context.drawable(typedValue.resourceId)
        }
    }
}