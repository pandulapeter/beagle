package com.pandulapeter.debugMenu.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.views.items.DrawerItem


internal class DebugMenuDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    onKeylineOverlaySwitchChanged: (isEnabled: Boolean) -> Unit = {},
    onAuthenticationHelperHeaderPressed: () -> Unit = {},
    onAuthenticationHelperItemClicked: (item: Pair<String, String>) -> Unit = {},
    onNetworkLoggingHeaderPressed: () -> Unit = {},
    onNetworkLogEventClicked: (networkEvent: NetworkEvent) -> Unit = {},
    onLoggingHeaderPressed: () -> Unit = {}
) : RecyclerView(context, attrs, defStyleAttr) {

    private val debugMenuAdapter = DebugMenuAdapter(
        onSettingsLinkButtonPressed = {
            context.startActivity(Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                data = Uri.fromParts("package", context.packageName, null)
            })
        },
        onAuthenticationHelperHeaderPressed = onAuthenticationHelperHeaderPressed,
        onAuthenticationHelperItemClicked = onAuthenticationHelperItemClicked,
        onKeylineOverlaySwitchChanged = onKeylineOverlaySwitchChanged,
        onNetworkLoggingHeaderPressed = onNetworkLoggingHeaderPressed,
        onNetworkLogEventClicked = onNetworkLogEventClicked,
        onLoggingHeaderPressed = onLoggingHeaderPressed
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

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets = super.dispatchApplyWindowInsets(insets?.also {
        setPadding(paddingLeft, it.systemWindowInsetTop, paddingRight, it.systemWindowInsetBottom)
    })

    fun updateItems(items: List<DrawerItem>) = debugMenuAdapter.submitList(items)
}