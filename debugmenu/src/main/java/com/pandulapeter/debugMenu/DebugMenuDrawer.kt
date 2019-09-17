package com.pandulapeter.debugMenu

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.TextView

internal class DebugMenuDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    init {
        text = "Debug menu"
        setBackgroundColor(Color.RED)
        setTextColor(Color.CYAN)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requestApplyInsets()
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets = super.dispatchApplyWindowInsets(insets?.also {
        setPadding(paddingLeft, it.systemWindowInsetTop, paddingRight, paddingBottom)
    })
}