package com.pandulapeter.beagle.appDemo.feature.main.playground

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.ModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class PlaygroundViewModel : ListViewModel<PlaygroundListItem>() {

    override val items: LiveData<List<PlaygroundListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.playground_description),
            TextViewHolder.UiModel(R.string.playground_generic_modules),
            ModuleViewHolder.UiModel("Button"),
            ModuleViewHolder.UiModel("CheckBox"),
            ModuleViewHolder.UiModel("Divider"),
            ModuleViewHolder.UiModel("ItemList"),
            ModuleViewHolder.UiModel("KeyValueList"),
            ModuleViewHolder.UiModel("Label"),
            ModuleViewHolder.UiModel("LongText"),
            ModuleViewHolder.UiModel("MultipleSelectionList"),
            ModuleViewHolder.UiModel("Padding"),
            ModuleViewHolder.UiModel("SingleSelectionList"),
            ModuleViewHolder.UiModel("Switch"),
            ModuleViewHolder.UiModel("Text"),
            TextViewHolder.UiModel(R.string.playground_unique_modules),
            ModuleViewHolder.UiModel("AnimationDurationSwitch"),
            ModuleViewHolder.UiModel("AppInfo"),
            ModuleViewHolder.UiModel("DeviceInfo"),
            ModuleViewHolder.UiModel("ForceCrashButton"),
            ModuleViewHolder.UiModel("Header")
        )
    )
}