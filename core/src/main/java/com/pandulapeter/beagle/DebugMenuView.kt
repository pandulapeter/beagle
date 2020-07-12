package com.pandulapeter.beagle

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.listeners.UpdateListener
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.colorResource
import com.pandulapeter.beagle.core.util.extension.dimension
import com.pandulapeter.beagle.core.util.extension.drawable
import com.pandulapeter.beagle.core.view.GestureBlockingRecyclerView

//TODO: in the ui-view module should notify the listeners when this view is attached / detached
class DebugMenuView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context.applyTheme(), attrs, defStyleAttr), UpdateListener {

    private val verticalMargin = context.dimension(R.dimen.beagle_item_vertical_margin)
    private var recyclerLeftPadding = 0
    private var recyclerTopPadding = verticalMargin
    private var recyclerRightPadding = 0
    private var recyclerBottomPadding = verticalMargin

    //TODO: Create a custom view for the button container and all related logic and improve their appearance
    private val largePadding = context.dimension(R.dimen.beagle_large_content_padding)
    private val applyButton = AppCompatButton(context.applyTheme(), attrs, androidx.appcompat.R.attr.buttonStyle).apply {
        isAllCaps = false
        text = BeagleCore.implementation.appearance.applyButtonText
        setPadding(largePadding, largePadding, largePadding, largePadding)
        setOnClickListener { BeagleCore.implementation.applyPendingChanges() }
    }
    private val resetButton = AppCompatButton(context.applyTheme(), attrs, androidx.appcompat.R.attr.buttonStyle).apply {
        isAllCaps = false
        text = BeagleCore.implementation.appearance.resetButtonText
        setPadding(largePadding, largePadding, largePadding, largePadding)
        setOnClickListener { BeagleCore.implementation.resetPendingChanges() }
    }
    private val buttonContainer = LinearLayout(context.applyTheme(), attrs, defStyleAttr).apply {
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
        clipChildren = false
        visibility = View.INVISIBLE
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
    private val recyclerView = GestureBlockingRecyclerView(context.applyTheme(), attrs, defStyleAttr).apply {
        clipToPadding = false
        BeagleCore.implementation.setupRecyclerView(this)
        minimumWidth = context.dimension(R.dimen.beagle_minimum_size)
        minimumHeight = context.dimension(R.dimen.beagle_minimum_size)
    }

    init {
        setBackgroundFromWindowBackground()
        addView(recyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(buttonContainer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply { gravity = Gravity.BOTTOM })
        onContentsChanged()
        applyInsets(0, 0, 0, 0)
    }

    fun applyInsets(left: Int, top: Int, right: Int, bottom: Int) {
        val scrollBy = recyclerTopPadding - top - verticalMargin
        recyclerLeftPadding = left
        recyclerTopPadding = top + verticalMargin
        recyclerRightPadding = right
        recyclerBottomPadding = bottom + verticalMargin
        buttonContainer.setPadding(left, top, right, bottom + largePadding)
        recyclerView.updatePadding()
        recyclerView.scrollBy(0, scrollBy)
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
        recyclerView.updatePadding()
        buttonContainer.run {
            if (hasPendingChanges) {
                visibility = View.VISIBLE
            }
            post {
                animate()
                    .alpha(if (hasPendingChanges) 1f else 0f)
                    .start()
                applyButton.animate().translationY(if (hasPendingChanges) 0f else height.toFloat()).start()
                resetButton.animate().translationY(if (hasPendingChanges) 0f else height.toFloat()).start()
            }
        }
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

    private fun RecyclerView.updatePadding() = setPadding(
        recyclerLeftPadding,
        recyclerTopPadding,
        recyclerRightPadding,
        if (BeagleCore.implementation.hasPendingUpdates) verticalMargin + buttonContainer.height else recyclerBottomPadding
    )
}