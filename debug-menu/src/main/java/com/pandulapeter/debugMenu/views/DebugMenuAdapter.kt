package com.pandulapeter.debugMenu.views

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenu.R
import com.pandulapeter.debugMenu.models.LogItem
import com.pandulapeter.debugMenu.models.NetworkLogItem
import com.pandulapeter.debugMenu.views.items.DrawerItem
import com.pandulapeter.debugMenu.views.items.button.ButtonViewHolder
import com.pandulapeter.debugMenu.views.items.button.ButtonViewModel
import com.pandulapeter.debugMenu.views.items.header.HeaderViewHolder
import com.pandulapeter.debugMenu.views.items.header.HeaderViewModel
import com.pandulapeter.debugMenu.views.items.listHeader.ListHeaderViewHolder
import com.pandulapeter.debugMenu.views.items.listHeader.ListHeaderViewModel
import com.pandulapeter.debugMenu.views.items.listItem.ListItemViewHolder
import com.pandulapeter.debugMenu.views.items.listItem.ListItemViewModel
import com.pandulapeter.debugMenu.views.items.logItem.LogItemViewHolder
import com.pandulapeter.debugMenu.views.items.logItem.LogItemViewModel
import com.pandulapeter.debugMenu.views.items.networkLogItem.NetworkLogItemViewHolder
import com.pandulapeter.debugMenu.views.items.networkLogItem.NetworkLogItemViewModel
import com.pandulapeter.debugMenu.views.items.text.TextViewHolder
import com.pandulapeter.debugMenu.views.items.text.TextViewModel
import com.pandulapeter.debugMenu.views.items.toggle.ToggleViewHolder
import com.pandulapeter.debugMenu.views.items.toggle.ToggleViewModel
import com.pandulapeter.debugMenuCore.configuration.modules.ListModule

internal class DebugMenuAdapter(
    private val onExpandCollapseHeaderPressed: (id: String) -> Unit,
    private val onListItemSelected: (itemListModule: ListModule<*>, itemId: String) -> Unit,
    private val onNetworkLogEventClicked: (networkLogItem: NetworkLogItem) -> Unit,
    private val onLogMessageClicked: (logItem: LogItem) -> Unit
) : ListAdapter<DrawerItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<DrawerItem>() {

    override fun areItemsTheSame(oldItem: DrawerItem, newItem: DrawerItem) = oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DrawerItem, newItem: DrawerItem) = oldItem == newItem

    override fun getChangePayload(oldItem: DrawerItem, newItem: DrawerItem): Any? = if (oldItem.shouldUsePayloads && newItem.shouldUsePayloads) "" else null
}) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is TextViewModel -> R.layout.item_text
        is ToggleViewModel -> R.layout.item_toggle
        is ButtonViewModel -> R.layout.item_button
        is ListHeaderViewModel -> R.layout.item_list_header
        is ListItemViewModel -> R.layout.item_list_item
        is HeaderViewModel -> R.layout.item_header
        is NetworkLogItemViewModel -> R.layout.item_network_log_event
        is LogItemViewModel -> R.layout.item_log_message
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_text -> TextViewHolder.create(parent)
        R.layout.item_toggle -> ToggleViewHolder.create(parent)
        R.layout.item_button -> ButtonViewHolder.create(parent)
        R.layout.item_list_header -> ListHeaderViewHolder.create(parent) { position -> onExpandCollapseHeaderPressed(getItem(position).id) }
        R.layout.item_list_item -> ListItemViewHolder.create(parent) { position ->
            (getItem(position) as ListItemViewModel).let {
                onListItemSelected(it.itemListModule, it.item.id)
            }
        }
        R.layout.item_header -> HeaderViewHolder.create(parent)
        R.layout.item_network_log_event -> NetworkLogItemViewHolder.create(parent) { position -> onNetworkLogEventClicked((getItem(position) as NetworkLogItemViewModel).networkLogItem) }
        R.layout.item_log_message -> LogItemViewHolder.create(parent) { position -> onLogMessageClicked((getItem(position) as LogItemViewModel).logItem) }
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is TextViewHolder -> holder.bind(getItem(position) as TextViewModel, DebugMenu.textColor)
        is ToggleViewHolder -> holder.bind(getItem(position) as ToggleViewModel, DebugMenu.textColor)
        is ButtonViewHolder -> holder.bind(getItem(position) as ButtonViewModel, DebugMenu.textColor)
        is ListHeaderViewHolder -> holder.bind(getItem(position) as ListHeaderViewModel, DebugMenu.textColor)
        is ListItemViewHolder -> holder.bind(getItem(position) as ListItemViewModel, DebugMenu.textColor)
        is HeaderViewHolder -> holder.bind(getItem(position) as HeaderViewModel, DebugMenu.textColor)
        is NetworkLogItemViewHolder -> holder.bind(getItem(position) as NetworkLogItemViewModel, DebugMenu.textColor)
        is LogItemViewHolder -> holder.bind(getItem(position) as LogItemViewModel, DebugMenu.textColor)
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }
}