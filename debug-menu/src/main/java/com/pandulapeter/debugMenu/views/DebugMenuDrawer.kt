package com.pandulapeter.debugMenu.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.AttributeSet
import android.util.TypedValue
import android.view.WindowInsets
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.utils.NetworkEvent
import com.pandulapeter.debugMenu.utils.color
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.DebugMenuConfiguration


internal class DebugMenuDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    configuration: DebugMenuConfiguration = DebugMenuConfiguration(),
    onLoggingHeaderPressed: () -> Unit = {},
    onNetworkLoggingHeaderPressed: () -> Unit = {},
    onNetworkLogEventClicked: (networkEvent: NetworkEvent) -> Unit = {}
) : RecyclerView(context, attrs, defStyleAttr) {

    private val debugMenuAdapter = DebugMenuAdapter(
        textColor = configuration.textColor.let { configurationTextColor ->
            if (configurationTextColor == null) {
                val typedValue = TypedValue()
                context.theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true)
                context.color(typedValue.run { if (resourceId != 0) resourceId else data })
            } else {
                configurationTextColor
            }
        },
        onSettingsLinkButtonPressed = {
            context.startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", context.packageName, null)
            })
        },
        onLoggingHeaderPressed = onLoggingHeaderPressed,
        onNetworkLoggingHeaderPressed = onNetworkLoggingHeaderPressed,
        onNetworkLogEventClicked = onNetworkLogEventClicked
    )

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

        // Initialize the layout
        clipToPadding = false
        layoutManager = LinearLayoutManager(context)
        adapter = debugMenuAdapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requestApplyInsets()
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets = super.dispatchApplyWindowInsets(insets?.also {
        setPadding(paddingLeft, it.systemWindowInsetTop, paddingRight, it.systemWindowInsetBottom)
    })

    fun updateItems(items: List<DrawerItem>) = debugMenuAdapter.submitList(items)
}