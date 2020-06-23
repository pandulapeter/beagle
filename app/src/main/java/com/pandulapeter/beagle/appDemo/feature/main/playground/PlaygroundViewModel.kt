package com.pandulapeter.beagle.appDemo.feature.main.playground

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.AddModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.GenerateCodeViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.ModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class PlaygroundViewModel : ListViewModel<PlaygroundListItem>() {

    override val items: LiveData<List<PlaygroundListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.playground_description),
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
            ModuleViewHolder.UiModel("AnimationDurationSwitch"),
            ModuleViewHolder.UiModel("AppInfo"),
            ModuleViewHolder.UiModel("DeviceInfo"),
            ModuleViewHolder.UiModel("ForceCrashButton"),
            ModuleViewHolder.UiModel("Header"),
            AddModuleViewHolder.UiModel(),
            GenerateCodeViewHolder.UiModel()
        )
    )
}