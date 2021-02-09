package com.pandulapeter.beagle.core.view

import android.animation.LayoutTransition
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.pandulapeter.beagle.core.databinding.BeagleViewMediaBinding
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.utils.extensions.inflater

internal class MediaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context.applyTheme(), attrs, defStyleAttr) {

    private val binding = BeagleViewMediaBinding.inflate(inflater, this)
    val checkBox = binding.beagleCheckBox
    val textView = binding.beagleTextView
    val imageView = binding.beagleImageView

    init {
        layoutTransition = LayoutTransition()
    }
}