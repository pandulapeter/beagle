package com.pandulapeter.beagle

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView

@SuppressLint("SetTextI18n", "unused")
class DebugMenuView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        gravity = Gravity.CENTER
        text = "The DebugMenuView should not be visible\nwhen using the noop dependency."
    }

    @Suppress("unused", "UNUSED_PARAMETER")
    fun applyInsets(left: Int, top: Int, right: Int, bottom: Int) = Unit
}