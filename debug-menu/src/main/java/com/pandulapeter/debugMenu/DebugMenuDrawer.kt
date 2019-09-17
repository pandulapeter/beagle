package com.pandulapeter.debugMenu

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.WindowInsets
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.pandulapeter.debugMenuCore.DebugMenuConfiguration


internal class DebugMenuDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    configuration: DebugMenuConfiguration = DebugMenuConfiguration()
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            setBackgroundColor(typedValue.data)
        } else {
            background = AppCompatResources.getDrawable(context, typedValue.resourceId)
        }
        configuration.title?.also { title -> addView(TextView(context).apply { text = title }) }
        configuration.version?.also { version -> addView(TextView(context).apply { text = version }) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requestApplyInsets()
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets = super.dispatchApplyWindowInsets(insets?.also {
        setPadding(paddingLeft, it.systemWindowInsetTop, paddingRight, paddingBottom)
    })
}