package com.pandulapeter.debugMenu.views

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.models.LogMessage
import com.pandulapeter.debugMenu.models.NetworkEvent
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenu.views.items.authenticationHelperHeader.AuthenticationHelperHeaderViewHolder
import com.pandulapeter.debugMenu.views.items.authenticationHelperHeader.AuthenticationHelperHeaderViewModel
import com.pandulapeter.debugMenu.views.items.authenticationHelperItem.AuthenticationHelperItemViewHolder
import com.pandulapeter.debugMenu.views.items.authenticationHelperItem.AuthenticationHelperItemViewModel
import com.pandulapeter.debugMenu.views.items.header.HeaderViewHolder
import com.pandulapeter.debugMenu.views.items.header.HeaderViewModel
import com.pandulapeter.debugMenu.views.items.keylineOverlay.KeylineOverlayViewHolder
import com.pandulapeter.debugMenu.views.items.keylineOverlay.KeylineOverlayViewModel
import com.pandulapeter.debugMenu.views.items.logMessage.LogMessageViewHolder
import com.pandulapeter.debugMenu.views.items.logMessage.LogMessageViewModel
import com.pandulapeter.debugMenu.views.items.loggingHeader.LoggingHeaderViewHolder
import com.pandulapeter.debugMenu.views.items.loggingHeader.LoggingHeaderViewModel
import com.pandulapeter.debugMenu.views.items.networkLogEvent.NetworkLogEventViewHolder
import com.pandulapeter.debugMenu.views.items.networkLogEvent.NetworkLogEventViewModel
import com.pandulapeter.debugMenu.views.items.networkLoggingHeader.NetworkLoggingHeaderViewHolder
import com.pandulapeter.debugMenu.views.items.networkLoggingHeader.NetworkLoggingHeaderViewModel
import com.pandulapeter.debugMenu.views.items.settingsLink.SettingsLinkViewHolder
import com.pandulapeter.debugMenu.views.items.settingsLink.SettingsLinkViewModel

internal class DebugMenuAdapter(
    private val onSettingsLinkButtonPressed: () -> Unit,
    private val onKeylineOverlaySwitchChanged: (isEnabled: Boolean) -> Unit,
    private val onAuthenticationHelperHeaderPressed: () -> Unit,
    private val onAuthenticationHelperItemClicked: (item: Pair<String, String>) -> Unit,
    private val onNetworkLoggingHeaderPressed: () -> Unit,
    private val onNetworkLogEventClicked: (networkEvent: NetworkEvent) -> Unit,
    private val onLoggingHeaderPressed: () -> Unit,
    private val onLogMessageClicked: (logMessage: LogMessage) -> Unit
) : ListAdapter<DrawerItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<DrawerItem>() {

    override fun areItemsTheSame(oldItem: DrawerItem, newItem: DrawerItem) = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DrawerItem, newItem: DrawerItem) = oldItem == newItem

    override fun getChangePayload(oldItem: DrawerItem, newItem: DrawerItem): Any? = if (
        (oldItem is KeylineOverlayViewModel && newItem is KeylineOverlayViewModel)
        || (oldItem is NetworkLoggingHeaderViewModel && newItem is NetworkLoggingHeaderViewModel)
        || (oldItem is LoggingHeaderViewModel && newItem is LoggingHeaderViewModel)
    ) "" else null
}) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is HeaderViewModel -> R.layout.item_header
        is SettingsLinkViewModel -> R.layout.item_settings_link
        is KeylineOverlayViewModel -> R.layout.item_keyline_overlay
        is AuthenticationHelperHeaderViewModel -> R.layout.item_authentication_helper_header
        is AuthenticationHelperItemViewModel -> R.layout.item_authentication_helper_item
        is NetworkLoggingHeaderViewModel -> R.layout.item_network_logging_header
        is NetworkLogEventViewModel -> R.layout.item_network_log_event
        is LoggingHeaderViewModel -> R.layout.item_logging_header
        is LogMessageViewModel -> R.layout.item_log_message
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_header -> HeaderViewHolder.create(parent)
        R.layout.item_settings_link -> SettingsLinkViewHolder.create(parent, onSettingsLinkButtonPressed)
        R.layout.item_keyline_overlay -> KeylineOverlayViewHolder.create(parent, onKeylineOverlaySwitchChanged)
        R.layout.item_authentication_helper_header -> AuthenticationHelperHeaderViewHolder.create(parent, onAuthenticationHelperHeaderPressed)
        R.layout.item_authentication_helper_item -> AuthenticationHelperItemViewHolder.create(parent) { position -> onAuthenticationHelperItemClicked((getItem(position) as AuthenticationHelperItemViewModel).item) }
        R.layout.item_network_logging_header -> NetworkLoggingHeaderViewHolder.create(parent, onNetworkLoggingHeaderPressed)
        R.layout.item_network_log_event -> NetworkLogEventViewHolder.create(parent) { position -> onNetworkLogEventClicked((getItem(position) as NetworkLogEventViewModel).networkEvent) }
        R.layout.item_logging_header -> LoggingHeaderViewHolder.create(parent, onLoggingHeaderPressed)
        R.layout.item_log_message -> LogMessageViewHolder.create(parent) { position -> onLogMessageClicked((getItem(position) as LogMessageViewModel).logMessage) }
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is HeaderViewHolder -> holder.bind(getItem(position) as HeaderViewModel, DebugMenu.textColor)
        is SettingsLinkViewHolder -> holder.bind(getItem(position) as SettingsLinkViewModel, DebugMenu.textColor)
        is KeylineOverlayViewHolder -> holder.bind(getItem(position) as KeylineOverlayViewModel, DebugMenu.textColor)
        is AuthenticationHelperHeaderViewHolder -> holder.bind(getItem(position) as AuthenticationHelperHeaderViewModel, DebugMenu.textColor)
        is AuthenticationHelperItemViewHolder -> holder.bind(getItem(position) as AuthenticationHelperItemViewModel, DebugMenu.textColor)
        is NetworkLoggingHeaderViewHolder -> holder.bind(getItem(position) as NetworkLoggingHeaderViewModel, DebugMenu.textColor)
        is NetworkLogEventViewHolder -> holder.bind(getItem(position) as NetworkLogEventViewModel, DebugMenu.textColor)
        is LoggingHeaderViewHolder -> holder.bind(getItem(position) as LoggingHeaderViewModel, DebugMenu.textColor)
        is LogMessageViewHolder -> holder.bind(getItem(position) as LogMessageViewModel, DebugMenu.textColor)
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }
}