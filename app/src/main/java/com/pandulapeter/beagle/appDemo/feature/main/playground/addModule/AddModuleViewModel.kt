package com.pandulapeter.beagle.appDemo.feature.main.playground.addModule

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.ModuleRepository
import com.pandulapeter.beagle.appDemo.data.model.ModuleWrapper
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.AddModuleListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.ModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class AddModuleViewModel(private val moduleRepository: ModuleRepository) : ListViewModel<AddModuleListItem>() {

    override val items: LiveData<List<AddModuleListItem>> = MutableLiveData(
        mutableListOf<AddModuleListItem>().apply {
            add(TextViewHolder.UiModel(R.string.add_module_generic_modules))
            add(createModuleUiModel(ModuleWrapper.Button()))
            add(createModuleUiModel(ModuleWrapper.CheckBox()))
            add(createModuleUiModel(ModuleWrapper.Divider()))
            add(createModuleUiModel(ModuleWrapper.ItemList()))
            add(createModuleUiModel(ModuleWrapper.KeyValueList()))
            add(createModuleUiModel(ModuleWrapper.Label()))
            add(createModuleUiModel(ModuleWrapper.LogList()))
            add(createModuleUiModel(ModuleWrapper.LongText()))
            add(createModuleUiModel(ModuleWrapper.MultipleSelectionList()))
            add(createModuleUiModel(ModuleWrapper.Padding()))
            add(createModuleUiModel(ModuleWrapper.SingleSelectionList()))
            add(createModuleUiModel(ModuleWrapper.Slider()))
            add(createModuleUiModel(ModuleWrapper.Switch()))
            add(createModuleUiModel(ModuleWrapper.Text()))
            add(createModuleUiModel(ModuleWrapper.TextInput()))
            add(TextViewHolder.UiModel(R.string.add_module_unique_modules))
            add(createModuleUiModel(ModuleWrapper.AnimationDurationSwitch))
            add(createModuleUiModel(ModuleWrapper.AppInfoButton))
            add(createModuleUiModel(ModuleWrapper.DeveloperOptionsButton))
            add(createModuleUiModel(ModuleWrapper.DeviceInfo))
            add(createModuleUiModel(ModuleWrapper.ForceCrashButton))
            add(createModuleUiModel(ModuleWrapper.Header))
            add(createModuleUiModel(ModuleWrapper.KeylineOverlaySwitch))
            add(createModuleUiModel(ModuleWrapper.NetworkLogList))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                add(createModuleUiModel(ModuleWrapper.ScreenRecordingButton))
            }
            add(createModuleUiModel(ModuleWrapper.ScreenshotButton))
        }
    )

    fun onModuleSelected(moduleWrapper: ModuleWrapper) = moduleRepository.addModule(moduleWrapper)

    private fun createModuleUiModel(moduleWrapper: ModuleWrapper) = ModuleViewHolder.UiModel(
        moduleWrapper = moduleWrapper,
        isEnabled = !Beagle.contains(moduleWrapper.module.id)
    )
}