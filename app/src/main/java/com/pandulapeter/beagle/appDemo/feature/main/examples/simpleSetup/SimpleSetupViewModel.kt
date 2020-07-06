package com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.LoadingIndicatorViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.SimpleSetupListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SectionHeaderViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SpaceViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import kotlin.properties.Delegates

class SimpleSetupViewModel : ListViewModel<SimpleSetupListItem>() {

    private val _items = MutableLiveData<List<SimpleSetupListItem>>()
    override val items: LiveData<List<SimpleSetupListItem>> = _items
    private var selectedSection by Delegates.observable<Section?>(null) { _, _, _ -> refreshItems() }

    init {
        refreshItems()
    }

    fun onSectionHeaderSelected(uiModel: SectionHeaderViewHolder.UiModel) {
        Section.fromResourceId(uiModel.titleResourceId).let {
            selectedSection = if (it == selectedSection) null else it
        }
    }

    fun refreshItems() {
        _items.value = mutableListOf<SimpleSetupListItem>().apply {
            addTopSection()
            addAppInfoButtonSection()
            addDeveloperOptionsButtonSection()
            addForceCrashButtonSection()
            addScreenshotButtonSection()
            addScreenRecordingButtonSection()
            addKeylineOverlaySwitchSection()
            addAnimationDurationSwitchSection()
            addDeviceInfoSection()
        }
    }

    private fun MutableList<SimpleSetupListItem>.addTopSection() {
        add(TextViewHolder.UiModel(R.string.case_study_simple_setup_text_1))
        add(
            CodeSnippetViewHolder.UiModel(
                "Beagle.set(\n" +
                        "    AppInfoButtonModule(),\n" +
                        "    DeveloperOptionsButtonModule(),\n" +
                        "    ForceCrashButtonModule(),\n" +
                        "    ScreenshotButtonModule(),\n" +
                        "    ScreenRecordingButtonModule(), // Only available on API 21 and above\n" +
                        "    KeylineOverlaySwitchModule(),\n" +
                        "    AnimationDurationSwitchModule()\n" +
                        "    DeviceInformationModule()\n" +
                        ")"
            )
        )
        add(TextViewHolder.UiModel(R.string.case_study_simple_setup_text_2))
    }

    private fun MutableList<SimpleSetupListItem>.addAppInfoButtonSection() = addSection(Section.APP_INFO_BUTTON) {
        add(TextViewHolder.UiModel(R.string.case_study_simple_setup_app_info_button_2))
    }

    private fun MutableList<SimpleSetupListItem>.addDeveloperOptionsButtonSection() = addSection(Section.DEVELOPER_OPTIONS_BUTTON) {
        add(TextViewHolder.UiModel(R.string.case_study_simple_setup_developer_options_button_2))
    }

    private fun MutableList<SimpleSetupListItem>.addForceCrashButtonSection() = addSection(Section.FORCE_CRASH_BUTTON) {
        add(TextViewHolder.UiModel(R.string.case_study_simple_setup_force_crash_button_2))
    }

    private fun MutableList<SimpleSetupListItem>.addScreenshotButtonSection() = addSection(Section.SCREENSHOT_BUTTON) {
        add(TextViewHolder.UiModel(R.string.case_study_simple_setup_screenshot_button_2))
    }

    private fun MutableList<SimpleSetupListItem>.addScreenRecordingButtonSection() = addSection(Section.SCREEN_RECORDING_BUTTON) {
        add(TextViewHolder.UiModel(R.string.case_study_simple_setup_screen_recording_button_2))
    }

    private fun MutableList<SimpleSetupListItem>.addKeylineOverlaySwitchSection() = addSection(Section.KEYLINE_OVERLAY_SWITCH) {
        add(TextViewHolder.UiModel(R.string.case_study_simple_setup_keyline_overlay_switch_2))
    }

    private fun MutableList<SimpleSetupListItem>.addAnimationDurationSwitchSection() = addSection(Section.ANIMATION_DURATION_SWITCH) {
        add(TextViewHolder.UiModel(R.string.case_study_simple_setup_animation_duration_switch_2))
        add(LoadingIndicatorViewHolder.UiModel())
    }

    private fun MutableList<SimpleSetupListItem>.addDeviceInfoSection() = addSection(Section.DEVICE_INFO) {
        add(TextViewHolder.UiModel(R.string.case_study_simple_setup_device_info_2))
    }

    private fun MutableList<SimpleSetupListItem>.addSection(section: Section, action: MutableList<SimpleSetupListItem>.() -> Unit) = (selectedSection == section).also { isExpanded ->
        add(SectionHeaderViewHolder.UiModel(section.titleResourceId, isExpanded))
        if (isExpanded) {
            action()
            add(SpaceViewHolder.UiModel())
        }
    }

    private enum class Section(@StringRes val titleResourceId: Int) {
        APP_INFO_BUTTON(R.string.case_study_simple_setup_app_info_button_1),
        DEVELOPER_OPTIONS_BUTTON(R.string.case_study_simple_setup_developer_options_button_1),
        FORCE_CRASH_BUTTON(R.string.case_study_simple_setup_force_crash_button_1),
        SCREENSHOT_BUTTON(R.string.case_study_simple_setup_screenshot_button_1),
        SCREEN_RECORDING_BUTTON(R.string.case_study_simple_setup_screen_recording_button_1),
        KEYLINE_OVERLAY_SWITCH(R.string.case_study_simple_setup_keyline_overlay_switch_1),
        ANIMATION_DURATION_SWITCH(R.string.case_study_simple_setup_animation_duration_switch_1),
        DEVICE_INFO(R.string.case_study_simple_setup_device_info_1);

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.titleResourceId == titleResourceId }
        }
    }
}