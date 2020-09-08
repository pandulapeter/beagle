package com.pandulapeter.beagle.appDemo.feature.shared

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.HorizontalScrollView
import androidx.appcompat.widget.AppCompatTextView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.utils.extensions.color
import com.pandulapeter.beagle.utils.extensions.dimension

class CodeSnippetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private val textView = AppCompatTextView(context, attrs, defStyleAttr).apply {
        typeface = Typeface.create("monospace", Typeface.NORMAL)
        setTextColor(context.color(R.color.black))
        setTextSize(TypedValue.COMPLEX_UNIT_PX, context.dimension(R.dimen.code_snippet).toFloat())
        context.dimension(R.dimen.content_padding).let { setPadding(it, it, it, it) }
        setTextIsSelectable(true)
        ellipsize = TextUtils.TruncateAt.MARQUEE
    }
    var text: CharSequence
        get() = textView.text ?: ""
        set(value) {
            textView.text = value
        }

    init {
        addView(textView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        setBackgroundResource(R.drawable.bg_code_snippet)
        overScrollMode = View.OVER_SCROLL_NEVER
    }
}