package com.pandulapeter.beagle

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View

class BeagleView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    //TODO: in the ui-view module call notify the listeners on attach / detach
    init {
        //TODO: Observe changes
        if (BeagleCore.implementation.isUiEnabled) {
            setBackgroundColor(Color.GREEN)
        } else {
            setBackgroundColor(Color.RED)
        }
        setOnClickListener { BeagleCore.implementation.hide() }
    }
}