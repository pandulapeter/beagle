package com.pandulapeter.debugMenu.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.WindowInsets
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.utils.color
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenu.views.items.header.HeaderViewModel
import com.pandulapeter.debugMenuCore.DebugMenuConfiguration
import com.pandulapeter.debugMenuCore.modules.HeaderModule


internal class DebugMenuDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    configuration: DebugMenuConfiguration = DebugMenuConfiguration()
) : RecyclerView(context, attrs, defStyleAttr) {

    init {
        // Set background color
        val backgroundColor = configuration.backgroundColor
        if (backgroundColor == null) {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
            if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                setBackgroundColor(typedValue.data)
            } else {
                background = AppCompatResources.getDrawable(context, typedValue.resourceId)
            }
        } else {
            setBackgroundColor(backgroundColor)
        }

        // Set text color
        val configurationTextColor = configuration.textColor
        val textColor = if (configurationTextColor == null) {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
            context.color(typedValue.run { if (resourceId != 0) resourceId else data })
        } else {
            configurationTextColor
        }

        // Add the modules
        clipToPadding = false
        layoutManager = LinearLayoutManager(context)
        adapter = DebugMenuAdapter().apply {
            val items = mutableListOf<DrawerItem>()
            configuration.modules.forEach { module ->

                // Set up header module
                if (module is HeaderModule) {
                    items.add(HeaderViewModel(textColor, module))
                }
            }
            submitList(items)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requestApplyInsets()
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets = super.dispatchApplyWindowInsets(insets?.also {
        setPadding(paddingLeft, it.systemWindowInsetTop, paddingRight, it.systemWindowInsetBottom)
    })
}