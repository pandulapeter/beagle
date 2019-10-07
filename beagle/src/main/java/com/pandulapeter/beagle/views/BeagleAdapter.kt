package com.pandulapeter.beagle.views

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel
import com.pandulapeter.beagle.views.drawerItems.button.ButtonListItemViewHolder
import com.pandulapeter.beagle.views.drawerItems.button.ButtonViewHolder
import com.pandulapeter.beagle.views.drawerItems.button.ButtonViewModel
import com.pandulapeter.beagle.views.drawerItems.divider.DividerViewHolder
import com.pandulapeter.beagle.views.drawerItems.divider.DividerViewModel
import com.pandulapeter.beagle.views.drawerItems.header.HeaderViewHolder
import com.pandulapeter.beagle.views.drawerItems.header.HeaderViewModel
import com.pandulapeter.beagle.views.drawerItems.image.ImageViewHolder
import com.pandulapeter.beagle.views.drawerItems.image.ImageViewModel
import com.pandulapeter.beagle.views.drawerItems.keyValue.KeyValueItemViewHolder
import com.pandulapeter.beagle.views.drawerItems.keyValue.KeyValueItemViewModel
import com.pandulapeter.beagle.views.drawerItems.listHeader.ListHeaderViewHolder
import com.pandulapeter.beagle.views.drawerItems.listHeader.ListHeaderViewModel
import com.pandulapeter.beagle.views.drawerItems.listItem.ListItemViewHolder
import com.pandulapeter.beagle.views.drawerItems.listItem.ListItemViewModel
import com.pandulapeter.beagle.views.drawerItems.logItem.LogItemViewHolder
import com.pandulapeter.beagle.views.drawerItems.logItem.LogItemViewModel
import com.pandulapeter.beagle.views.drawerItems.longText.LongTextViewHolder
import com.pandulapeter.beagle.views.drawerItems.longText.LongTextViewModel
import com.pandulapeter.beagle.views.drawerItems.multipleSelectionListItem.MultipleSelectionListItemViewHolder
import com.pandulapeter.beagle.views.drawerItems.multipleSelectionListItem.MultipleSelectionListItemViewModel
import com.pandulapeter.beagle.views.drawerItems.networkLogItem.NetworkLogItemViewHolder
import com.pandulapeter.beagle.views.drawerItems.networkLogItem.NetworkLogItemViewModel
import com.pandulapeter.beagle.views.drawerItems.singleSelectionListItem.SingleSelectionListItemViewHolder
import com.pandulapeter.beagle.views.drawerItems.singleSelectionListItem.SingleSelectionListItemViewModel
import com.pandulapeter.beagle.views.drawerItems.slider.SliderViewHolder
import com.pandulapeter.beagle.views.drawerItems.slider.SliderViewModel
import com.pandulapeter.beagle.views.drawerItems.text.TextViewHolder
import com.pandulapeter.beagle.views.drawerItems.text.TextViewModel
import com.pandulapeter.beagle.views.drawerItems.toggle.ToggleViewHolder
import com.pandulapeter.beagle.views.drawerItems.toggle.ToggleViewModel

internal class BeagleAdapter : ListAdapter<DrawerItemViewModel, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<DrawerItemViewModel>() {

    override fun areItemsTheSame(oldItem: DrawerItemViewModel, newItem: DrawerItemViewModel) = oldItem.id == newItem.id

    //TODO: Expanded list items are re-bound on every change for some reason.
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DrawerItemViewModel, newItem: DrawerItemViewModel) = oldItem == newItem

    override fun getChangePayload(oldItem: DrawerItemViewModel, newItem: DrawerItemViewModel): Any? = if (oldItem.shouldUsePayloads && newItem.shouldUsePayloads) "" else null
}) {

    override fun getItemViewType(position: Int) = when (val item = getItem(position)) {
        is DividerViewModel -> R.layout.beagle_item_divider
        is TextViewModel -> R.layout.beagle_item_text
        is LongTextViewModel -> R.layout.beagle_item_long_text
        is ImageViewModel -> R.layout.beagle_item_image
        is SliderViewModel -> R.layout.beagle_item_slider
        is ToggleViewModel -> R.layout.beagle_item_toggle
        is ButtonViewModel -> if (item.shouldUseListItem) R.layout.beagle_item_button_list_item else R.layout.beagle_item_button
        is KeyValueItemViewModel -> R.layout.beagle_item_key_value
        is ListHeaderViewModel -> R.layout.beagle_item_list_header
        is ListItemViewModel<*> -> R.layout.beagle_item_list_item
        is SingleSelectionListItemViewModel<*> -> R.layout.beagle_item_single_selection_list_item
        is MultipleSelectionListItemViewModel<*> -> R.layout.beagle_item_multiple_selection_list_item
        is HeaderViewModel -> R.layout.beagle_item_header
        is NetworkLogItemViewModel -> R.layout.beagle_item_network_log
        is LogItemViewModel -> R.layout.beagle_item_log
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.beagle_item_divider -> DividerViewHolder.create(parent)
        R.layout.beagle_item_text -> TextViewHolder.create(parent)
        R.layout.beagle_item_long_text -> LongTextViewHolder.create(parent)
        R.layout.beagle_item_image -> ImageViewHolder.create(parent)
        R.layout.beagle_item_slider -> SliderViewHolder.create(parent)
        R.layout.beagle_item_toggle -> ToggleViewHolder.create(parent)
        R.layout.beagle_item_button -> ButtonViewHolder.create(parent)
        R.layout.beagle_item_button_list_item -> ButtonListItemViewHolder.create(parent)
        R.layout.beagle_item_key_value -> KeyValueItemViewHolder.create(parent)
        R.layout.beagle_item_list_header -> ListHeaderViewHolder.create(parent)
        R.layout.beagle_item_list_item -> ListItemViewHolder.create(parent)
        R.layout.beagle_item_single_selection_list_item -> SingleSelectionListItemViewHolder.create(parent)
        R.layout.beagle_item_multiple_selection_list_item -> MultipleSelectionListItemViewHolder.create(parent)
        R.layout.beagle_item_header -> HeaderViewHolder.create(parent)
        R.layout.beagle_item_network_log -> NetworkLogItemViewHolder.create(parent)
        R.layout.beagle_item_log -> LogItemViewHolder.create(parent)
        else -> throw IllegalArgumentException("Unsupported view type: $viewType.")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (holder) {
        is DividerViewHolder -> Unit
        is TextViewHolder -> holder.bind(getItem(position) as TextViewModel)
        is LongTextViewHolder -> holder.bind(getItem(position) as LongTextViewModel)
        is ImageViewHolder -> holder.bind(getItem(position) as ImageViewModel)
        is SliderViewHolder -> holder.bind(getItem(position) as SliderViewModel)
        is ToggleViewHolder -> holder.bind(getItem(position) as ToggleViewModel)
        is ButtonViewHolder -> holder.bind(getItem(position) as ButtonViewModel)
        is ButtonListItemViewHolder -> holder.bind(getItem(position) as ButtonViewModel)
        is KeyValueItemViewHolder -> holder.bind(getItem(position) as KeyValueItemViewModel)
        is ListHeaderViewHolder -> holder.bind(getItem(position) as ListHeaderViewModel)
        is ListItemViewHolder -> holder.bind(getItem(position) as ListItemViewModel<*>)
        is SingleSelectionListItemViewHolder -> holder.bind(getItem(position) as SingleSelectionListItemViewModel<*>)
        is MultipleSelectionListItemViewHolder -> holder.bind(getItem(position) as MultipleSelectionListItemViewModel<*>)
        is HeaderViewHolder -> holder.bind(getItem(position) as HeaderViewModel)
        is NetworkLogItemViewHolder -> holder.bind(getItem(position) as NetworkLogItemViewModel)
        is LogItemViewHolder -> holder.bind(getItem(position) as LogItemViewModel)
        else -> throw IllegalArgumentException("Unsupported item type at position $position.")
    }
}