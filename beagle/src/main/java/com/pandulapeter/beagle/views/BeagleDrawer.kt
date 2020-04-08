package com.pandulapeter.beagle.views

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.WindowInsets
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.colorResource
import com.pandulapeter.beagle.utils.dimension
import com.pandulapeter.beagle.utils.setBackgroundFromWindowBackground
import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.Trick

//TODO: RecyclerView scroll position is not restored.
internal class BeagleDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val beagleAdapter = BeagleAdapter()
    private val recyclerView = RecyclerView(context, attrs, defStyleAttr).apply {
        clipToPadding = false
        layoutManager = LinearLayoutManager(context)
        adapter = beagleAdapter
    }
    private val padding = context.dimension(R.dimen.beagle_content_padding)
    private val largePadding = context.dimension(R.dimen.beagle_large_content_padding)
    private val applyButton = ExtendedFloatingActionButton(context, attrs, defStyleAttr).apply {
        text = Beagle.appearance.applyButtonText
        setPadding(largePadding, largePadding, largePadding, largePadding)
        setOnClickListener { Trick.applyPendingChanges() }
    }
    private val resetButton = ExtendedFloatingActionButton(context, attrs, defStyleAttr).apply {
        text = Beagle.appearance.resetButtonText
        setPadding(largePadding, largePadding, largePadding, largePadding)
        setOnClickListener { Trick.resetPendingChanges() }
    }
    private val buttonContainer = LinearLayout(context, attrs, defStyleAttr).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        addView(applyButton, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            marginStart = largePadding
            marginEnd = if (Beagle.shouldShowResetButton) padding else largePadding
        })
        if (Beagle.shouldShowResetButton) {
            addView(resetButton, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                marginStart = padding
                marginEnd = largePadding
            })
        }
        background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(Color.TRANSPARENT, context.colorResource(android.R.attr.textColorPrimary)))
    }

    init {
        layoutTransition = LayoutTransition()
        setBackgroundFromWindowBackground()
        addView(recyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(buttonContainer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.BOTTOM
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requestApplyInsets()
    }

    //TODO: This doesn't seem to be working in all cases.
    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets = super.dispatchApplyWindowInsets(insets?.also { windowsInsets ->
        recyclerView.run {
            setPadding(
                paddingLeft,
                windowsInsets.systemWindowInsetTop,
                paddingRight,
                context.dimension(R.dimen.beagle_apply_button_margin) + windowsInsets.systemWindowInsetBottom
            )
        }
        buttonContainer.run {
            setPadding(
                paddingLeft,
                paddingTop,
                paddingRight,
                largePadding + windowsInsets.systemWindowInsetBottom
            )
        }
    })

    fun updateItems(items: List<DrawerItemViewModel>) {
        beagleAdapter.submitList(items)
        buttonContainer.run {
            animate()
                .translationY(if (Beagle.hasPendingChanges) 0f else height.toFloat())
                .alpha(if (Beagle.hasPendingChanges) 1f else 0f)
                .start()
        }
    }
}