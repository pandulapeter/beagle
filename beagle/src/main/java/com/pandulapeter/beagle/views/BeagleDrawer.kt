package com.pandulapeter.beagle.views

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.utils.dimension
import com.pandulapeter.beagle.utils.setBackgroundFromWindowBackground
import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel
import com.pandulapeter.beagleCore.configuration.Trick

//TODO: Scroll position is not restored.
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
    private val applyButton = ExtendedFloatingActionButton(context, attrs, defStyleAttr).apply {
        text = Beagle.appearance.applyButtonText
        context.dimension(R.dimen.beagle_large_content_padding).let { setPadding(it, it, it, it) }
        setOnClickListener { Trick.applyPendingChanges() }
    }

    init {
        setBackgroundFromWindowBackground()
        addView(recyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(applyButton, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        })
        layoutTransition = LayoutTransition()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requestApplyInsets()
    }

    //TODO: This doesn't seem to be working in all cases.
    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets = super.dispatchApplyWindowInsets(insets?.also { windowsInsets ->
        context.dimension(R.dimen.beagle_large_content_padding).also { largeContentPadding ->
            recyclerView.setPadding(
                paddingLeft,
                windowsInsets.systemWindowInsetTop,
                paddingRight,
                context.dimension(R.dimen.beagle_apply_button_margin) + windowsInsets.systemWindowInsetBottom
            )
            applyButton.run {
                layoutParams = (layoutParams as MarginLayoutParams).apply {
                    bottomMargin = largeContentPadding
                }
            }
        }
    })

    fun updateItems(items: List<DrawerItemViewModel>) {
        beagleAdapter.submitList(items)
        applyButton.visibility = if (Beagle.hasPendingChanges) View.VISIBLE else View.GONE
    }
}