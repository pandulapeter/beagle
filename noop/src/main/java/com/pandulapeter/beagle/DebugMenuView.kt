@file:Suppress("unused")

package com.pandulapeter.beagle

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView

@SuppressLint("SetTextI18n")
class DebugMenuView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        gravity = Gravity.CENTER
        text = "DebugMenuView should not be visible to users when using the noop dependency."
    }
}