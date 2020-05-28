package com.pandulapeter.beagle.implementation


import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.pandulapeter.beagle.BeagleView

internal class BeagleDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        addView(BeagleView(context, attrs, defStyleAttr), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }
}