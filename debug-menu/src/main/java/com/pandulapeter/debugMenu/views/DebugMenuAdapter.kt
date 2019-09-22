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
import com.pandulapeter.debugMenu.views.items.authenticationHelperItem.AuthenticationHelperItemViewHolder
import com.pandulapeter.debugMenu.views.items.authenticationHelperItem.AuthenticationHelperItemViewModel
import com.pandulapeter.debugMenu.views.items.expandCollapseHeader.ExpandCollapseHeaderViewHolder
import com.pandulapeter.debugMenu.views.items.expandCollapseHeader.ExpandCollapseHeaderViewModel
import com.pandulapeter.debugMenu.views.items.header.HeaderViewHolder
import com.pandulapeter.debugMenu.views.items.header.HeaderViewModel
import com.pandulapeter.debugMenu.views.items.keylineOverlay.KeylineOverlayViewHolder
import com.pandulapeter.debugMenu.views.items.keylineOverlay.KeylineOverlayViewModel
import com.pandulapeter.debugMenu.views.items.logMessage.LogMessageViewHolder
import com.pandulapeter.debugMenu.views.items.logMessage.LogMessageViewModel
import com.pandulapeter.debugMenu.views.items.networkLogEvent.NetworkLogEventViewHolder
import com.pandulapeter.debugMenu.views.items.networkLogEvent.NetworkLogEventViewModel
import com.pandulapeter.debugMenu.views.items.settingsLink.SettingsLinkViewHolder
import com.pandulapeter.debugMenu.views.items.settingsLink.SettingsLinkViewModel
import com.pandulapeter.debugMenuCore.configuration.modules.ItemListModule

internal class DebugMenuAdapter(
    private val onSettingsLinkButtonPressed: () -> Unit,
    private val onKeylineOverlaySwitchChanged: (isEnabled: Boolean) -> Unit,
    private val onExpandCollapseHeaderPressed: (id: String) -> Unit,
    private val onListItemPressed: (itemListModule: ItemListModule<*>, itemId: String) -> Unit,
    private val onNetworkLogEventClicked: (networkEvent: NetworkEvent) -> Unit,
    private val onLogMessageClicked: (logMessage: LogMessage) -> Unit
) : ListAdapter<DrawerItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<DrawerItem>() {

    override fun areItemsTheSame(oldItem: DrawerItem, newItem: DrawerItem) = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DrawerItem, newItem: DrawerItem) = oldItem == newItem

    override fun getChangePayload(oldItem: DrawerItem, newItem: DrawerItem): Any? = if (oldItem.shouldUsePayloads && newItem.shouldUsePayloads) "" else null
}) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is HeaderViewModel -> R.layout.item_header
        is SettingsLinkViewModel -> R.layout.item_button
        is KeylineOverlayViewModel -> R.layout.item_switch
        is ExpandCollapseHeaderViewModel -> R.layout.item_expand_collapse_header
        is AuthenticationHelperItemViewModel -> R.layout.item_list_element_simple
        is NetworkLogEventViewModel -> R.layout.item_network_log_event
        is LogMessageViewModel -> R.layout.item_log_message
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_header -> HeaderViewHolder.create(parent)
        R.layout.item_button -> SettingsLinkViewHolder.create(parent, onSettingsLinkButtonPressed)
        R.layout.item_switch -> KeylineOverlayViewHolder.create(parent, onKeylineOverlaySwitchChanged)
        R.layout.item_expand_collapse_header -> ExpandCollapseHeaderViewHolder.create(parent) { position -> onExpandCollapseHeaderPressed(getItem(position).id) }
        R.layout.item_list_element_simple -> AuthenticationHelperItemViewHolder.create(parent) { position ->
            (getItem(position) as AuthenticationHelperItemViewModel).let {
                onListItemPressed(it.itemListModule, it.item.id)
            }
        }
        R.layout.item_network_log_event -> NetworkLogEventViewHolder.create(parent) { position -> onNetworkLogEventClicked((getItem(position) as NetworkLogEventViewModel).networkEvent) }
        R.layout.item_log_message -> LogMessageViewHolder.create(parent) { position -> onLogMessageClicked((getItem(position) as LogMessageViewModel).logMessage) }
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is HeaderViewHolder -> holder.bind(getItem(position) as HeaderViewModel, DebugMenu.textColor)
        is SettingsLinkViewHolder -> holder.bind(getItem(position) as SettingsLinkViewModel, DebugMenu.textColor)
        is KeylineOverlayViewHolder -> holder.bind(getItem(position) as KeylineOverlayViewModel, DebugMenu.textColor)
        is ExpandCollapseHeaderViewHolder -> holder.bind(getItem(position) as ExpandCollapseHeaderViewModel, DebugMenu.textColor)
        is AuthenticationHelperItemViewHolder -> holder.bind(getItem(position) as AuthenticationHelperItemViewModel, DebugMenu.textColor)
        is NetworkLogEventViewHolder -> holder.bind(getItem(position) as NetworkLogEventViewModel, DebugMenu.textColor)
        is LogMessageViewHolder -> holder.bind(getItem(position) as LogMessageViewModel, DebugMenu.textColor)
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }
}