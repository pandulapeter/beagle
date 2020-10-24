package com.pandulapeter.beagle.core.view

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.applyTheme

internal class MediaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context.applyTheme(), attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.beagle_view_media, this, true)
        layoutTransition = LayoutTransition()
    }

    val checkBox = findViewById<CheckBox>(R.id.beagle_check_box)
    val textView = findViewById<TextView>(R.id.beagle_text_view)
    val imageView = findViewById<ImageView>(R.id.beagle_image_view)
}