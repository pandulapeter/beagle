package com.pandulapeter.debugMenu.views

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.models.LogMessage
import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenuCore.configuration.modules.ListModule


internal class DebugMenuDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    onExpandCollapseHeaderPressed: (id: String) -> Unit = {},
    onListItemPressed: (itemListModule: ListModule<*>, itemId: String) -> Unit = { _, _ -> },
    onNetworkLogEventClicked: (networkEvent: NetworkEvent) -> Unit = {},
    onLogMessageClicked: (logMessage: LogMessage) -> Unit = {}
) : RecyclerView(context, attrs, defStyleAttr) {

    private val debugMenuAdapter = DebugMenuAdapter(
        onListItemSelected = onListItemPressed,
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
    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets = super.dispatchApplyWindowInsets(insets?.also { windowsInsets ->
        setPadding(paddingLeft, windowsInsets.systemWindowInsetTop, paddingRight, windowsInsets.systemWindowInsetBottom)
    })

    fun updateItems(items: List<DrawerItem>) = debugMenuAdapter.submitList(items)
}