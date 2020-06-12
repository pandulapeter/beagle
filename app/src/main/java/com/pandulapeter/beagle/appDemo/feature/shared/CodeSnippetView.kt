package com.pandulapeter.beagle.appDemo.feature.shared

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.utils.color
import com.pandulapeter.beagle.appDemo.utils.dimension

//TODO: Wrap into HorizontalScrolLView
class CodeSnippetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        typeface = Typeface.create("monospace", Typeface.NORMAL);
        setTextColor(context.color(R.color.black))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.dimension(R.dimen.code_snippet).toFloat())
        setBackgroundColor(context.color(R.color.brand_light))
        context.dimension(R.dimen.content_padding).let { setPadding(it, it, it, it) }
        setTextIsSelectable(true)
    }
}