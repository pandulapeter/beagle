package com.pandulapeter.beagle

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BeagleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    init {
        setBackgroundColor(Color.GREEN)
    }
}