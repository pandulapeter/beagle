package com.pandulapeter.beagle.appDemo.feature.shared

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.utils.color
import com.pandulapeter.beagle.appDemo.utils.dimension

class CodeSnippetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        typeface = Typeface.create("monospace", Typeface.NORMAL);
        setTextColor(context.color(R.color.black))
        setBackgroundColor(context.color(R.color.brand_light))
        context.dimension(R.dimen.content_padding).let { setPadding(it * 2, it, it * 2, it) }
        setTextIsSelectable(true)
    }
}