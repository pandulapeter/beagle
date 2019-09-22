package com.pandulapeter.debugMenu.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.models.LogMessage
import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.ItemListModule


internal class DebugMenuDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    onKeylineOverlaySwitchChanged: (isEnabled: Boolean) -> Unit = {},
    onExpandCollapseHeaderPressed: (id: String) -> Unit = {},
    onListItemPressed: (itemListModule: ItemListModule<*>, itemId: String) -> Unit = { _, _ -> },
    onNetworkLogEventClicked: (networkEvent: NetworkEvent) -> Unit = {},
    onLogMessageClicked: (logMessage: LogMessage) -> Unit = {}
) : RecyclerView(context, attrs, defStyleAttr) {

    private val debugMenuAdapter = DebugMenuAdapter(
        onSettingsLinkButtonPressed = {
            context.startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", context.packageName, null)
            })
        },
        onListItemPressed = onListItemPressed,
        onKeylineOverlaySwitchChanged = onKeylineOverlaySwitchChanged,
        onExpandCollapseHeaderPressed = onExpandCollapseHeaderPressed,
        onNetworkLogEventClicked = onNetworkLogEventClicked,
        onLogMessageClicked = onLogMessageClicked
    )

    init {
        clipToPadding = false
        layoutManager = LinearLayoutManager(context)
        adapter = debugMenuAdapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requestApplyInsets()
    }

    //TODO: This doesn't seem to be working in all cases.
    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets = super.dispatchApplyWindowInsets(insets?.also {
        setPadding(paddingLeft, it.systemWindowInsetTop, paddingRight, it.systemWindowInsetBottom)
    })

    fun updateItems(items: List<DrawerItem>) = debugMenuAdapter.submitList(items)
}