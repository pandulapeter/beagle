package com.pandulapeter.beagle

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class BeagleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {

    //TODO: in the ui-view module call notify the listeners on attach / detach
    init {
        BeagleCore.implementation.setupRecyclerView(this)
        setBackgroundColor(Color.GRAY)
    }
}