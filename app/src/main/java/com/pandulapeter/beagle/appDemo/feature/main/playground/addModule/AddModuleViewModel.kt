package com.pandulapeter.beagle.appDemo.feature.main.playground.addModule

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
            add(createModuleUiModel(ModuleWrapper.CheckBoxWrapper()))
            add(createModuleUiModel(ModuleWrapper.DividerWrapper()))
            add(createModuleUiModel(ModuleWrapper.ItemListWrapper()))
            add(createModuleUiModel(ModuleWrapper.KeyValueListWrapper()))
            add(createModuleUiModel(ModuleWrapper.LoadingIndicatorWrapper()))
            add(createModuleUiModel(ModuleWrapper.LogListWrapper()))
            add(createModuleUiModel(ModuleWrapper.LongTextWrapper()))
            add(createModuleUiModel(ModuleWrapper.MultipleSelectionListWrapper()))
            add(createModuleUiModel(ModuleWrapper.PaddingWrapper()))
            add(createModuleUiModel(ModuleWrapper.SingleSelectionListWrapper()))
            add(createModuleUiModel(ModuleWrapper.SliderWrapper()))
            add(createModuleUiModel(ModuleWrapper.SwitchWrapper()))
            add(createModuleUiModel(ModuleWrapper.TextNormalWrapper()))
            add(createModuleUiModel(ModuleWrapper.TextSectionHeaderWrapper()))
            add(createModuleUiModel(ModuleWrapper.TextButtonWrapper()))
            add(createModuleUiModel(ModuleWrapper.TextInputWrapper()))
            add(TextViewHolder.UiModel(R.string.add_module_unique_modules))
            add(createModuleUiModel(ModuleWrapper.AnimationDurationSwitchWrapper))
            add(createModuleUiModel(ModuleWrapper.AppInfoButtonWrapper))
            add(createModuleUiModel(ModuleWrapper.BugReportButtonWrapper))
            add(createModuleUiModel(ModuleWrapper.DeveloperOptionsButtonWrapper))
            add(createModuleUiModel(ModuleWrapper.DeviceInfoWrapper))
            add(createModuleUiModel(ModuleWrapper.ForceCrashButtonWrapper))
            add(createModuleUiModel(ModuleWrapper.GalleryButtonWrapper))
            add(createModuleUiModel(ModuleWrapper.HeaderWrapper))
            add(createModuleUiModel(ModuleWrapper.KeylineOverlaySwitchWrapper))
            add(createModuleUiModel(ModuleWrapper.LoremIpsumGeneratorButtonWrapper()))
            add(createModuleUiModel(ModuleWrapper.NetworkLogListWrapper))
            add(createModuleUiModel(ModuleWrapper.ScreenCaptureToolboxWrapper))
            add(createModuleUiModel(ModuleWrapper.ScreenRecordingButtonWrapper))
            add(createModuleUiModel(ModuleWrapper.ScreenshotButtonWrapper))
        }
    )

    fun onModuleSelected(moduleWrapper: ModuleWrapper) = moduleRepository.addModule(moduleWrapper)

    private fun createModuleUiModel(moduleWrapper: ModuleWrapper) = ModuleViewHolder.UiModel(
        moduleWrapper = moduleWrapper,
        isEnabled = !Beagle.contains(moduleWrapper.module.id)
    )
}