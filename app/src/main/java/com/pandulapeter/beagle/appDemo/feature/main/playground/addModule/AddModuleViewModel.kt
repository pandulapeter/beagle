package com.pandulapeter.beagle.appDemo.feature.main.playground.addModule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.ModuleRepository
import com.pandulapeter.beagle.appDemo.data.ModuleWrapper
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.AddModuleListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.ModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class AddModuleViewModel(private val moduleRepository: ModuleRepository) : ListViewModel<AddModuleListItem>() {

    override val items: LiveData<List<AddModuleListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.add_module_generic_modules),
            createModuleUiModel(ModuleWrapper.Button()),
            createModuleUiModel(ModuleWrapper.CheckBox()),
            createModuleUiModel(ModuleWrapper.Divider()),
            createModuleUiModel(ModuleWrapper.ItemList()),
            createModuleUiModel(ModuleWrapper.KeyValueList()),
            createModuleUiModel(ModuleWrapper.Label()),
            createModuleUiModel(ModuleWrapper.LongText()),
            createModuleUiModel(ModuleWrapper.MultipleSelectionList()),
            createModuleUiModel(ModuleWrapper.Padding()),
            createModuleUiModel(ModuleWrapper.SingleSelectionList()),
            createModuleUiModel(ModuleWrapper.Switch()),
            createModuleUiModel(ModuleWrapper.Text()),
            TextViewHolder.UiModel(R.string.add_module_unique_modules),
            createModuleUiModel(ModuleWrapper.AnimationDurationSwitch),
            createModuleUiModel(ModuleWrapper.AppInfoButton),
            createModuleUiModel(ModuleWrapper.DeviceInfo),
            createModuleUiModel(ModuleWrapper.ForceCrashButton),
            createModuleUiModel(ModuleWrapper.Header)
        )
    )

    fun onModuleSelected(moduleWrapper: ModuleWrapper) = moduleRepository.addModule(moduleWrapper)

    private fun createModuleUiModel(moduleWrapper: ModuleWrapper) = ModuleViewHolder.UiModel(
        moduleWrapper = moduleWrapper,
        isEnabled = !Beagle.contains(moduleWrapper.module.id)
    )
}