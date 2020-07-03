package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.BasicSetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.LoadingIndicatorViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SectionHeaderViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SpaceViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import kotlin.properties.Delegates

class BasicSetupViewModel : ListViewModel<BasicSetupListItem>() {

    private val _items = MutableLiveData<List<BasicSetupListItem>>()
    override val items: LiveData<List<BasicSetupListItem>> = _items
    private var selectedSection by Delegates.observable<Section?>(null) { _, _, _ -> refreshItems() }
    private var hasSectionJustChanged = true

    init {
        refreshItems()
    }

    fun onSectionHeaderSelected(uiModel: SectionHeaderViewHolder.UiModel) {
        Section.fromResourceId(uiModel.titleResourceId).let {
            selectedSection = if (it == selectedSection) null else it
            hasSectionJustChanged = true
        }
    }

    fun shouldSetAppBarToNotLifted() = hasSectionJustChanged.also {
        hasSectionJustChanged = false
    }

    fun refreshItems() {
        _items.value = mutableListOf<BasicSetupListItem>().apply {
            addTopSection()
            addHeaderSection()
            addAppInfoButtonSection()
            addDeveloperOptionsButtonSection()
            addForceCrashButtonSection()
            addKeylineOverlaySwitchSection()
            addAnimationDurationSwitchSection()
            addDeviceInfoSection()
        }
    }

    private fun MutableList<BasicSetupListItem>.addTopSection() {
        add(TextViewHolder.UiModel(R.string.case_study_basic_setup_text_1))
        add(
            CodeSnippetViewHolder.UiModel(
                "Beagle.set(\n" +
                        "    HeaderModule(\n" +
                        "        title = headerTitle,\n" +
                        "        subtitle = headerSubtitle,\n" +
                        "        text = headerText\n" +
                        "    ),\n" +
                        "    AppInfoButtonModule(),\n" +
                        "    DeveloperOptionsButtonModule(),\n" +
                        "    ForceCrashButtonModule(),\n" +
                        "    KeylineOverlaySwitchModule(),\n" +
                        "    AnimationDurationSwitchModule()\n" +
                        "    DeviceInformationModule()\n" +
                        ")"
            )
        )
        add(TextViewHolder.UiModel(R.string.case_study_basic_setup_text_2))
        add(SpaceViewHolder.UiModel())
    }

    private fun MutableList<BasicSetupListItem>.addHeaderSection() {
        if (addSectionHeader(Section.HEADER)) {
            add(TextViewHolder.UiModel(R.string.case_study_basic_setup_header_2))
            add(
                CodeSnippetViewHolder.UiModel(
                    "val headerTitle = getString(R.string.app_name)\n" +
                            "val headerSubitle = \"v\${BuildConfig.VERSION_NAME} (\${BuildConfig.VERSION_CODE})\"\n" +
                            "val headerText = \"Built on \${BuildConfig.BUILD_DATE}\""
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_basic_setup_header_3))
            add(
                CodeSnippetViewHolder.UiModel(
                    "android {\n" +
                            "    …\n" +
                            "    defaultConfig {\n" +
                            "        …\n" +
                            "        buildConfigField(\"String\", \"BUILD_DATE\", \"\\\"\${new Date ().format(\"yyyy.MM.dd\")}\\\"\")\n" +
                            "    }\n" +
                            "}"
                )
            )
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<BasicSetupListItem>.addAppInfoButtonSection() {
        if (addSectionHeader(Section.APP_INFO_BUTTON)) {
            add(TextViewHolder.UiModel(R.string.case_study_basic_setup_app_info_button_2))
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<BasicSetupListItem>.addDeveloperOptionsButtonSection() {
        if (addSectionHeader(Section.DEVELOPER_OPTIONS_BUTTON)) {
            add(TextViewHolder.UiModel(R.string.case_study_basic_setup_developer_options_button_2))
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<BasicSetupListItem>.addForceCrashButtonSection() {
        if (addSectionHeader(Section.FORCE_CRASH_BUTTON)) {
            add(TextViewHolder.UiModel(R.string.case_study_basic_setup_force_crash_button_2))
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<BasicSetupListItem>.addKeylineOverlaySwitchSection() {
        if (addSectionHeader(Section.KEYLINE_OVERLAY_SWITCH)) {
            add(TextViewHolder.UiModel(R.string.case_study_basic_setup_keyline_overlay_switch_2))
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<BasicSetupListItem>.addAnimationDurationSwitchSection() {
        if (addSectionHeader(Section.ANIMATION_DURATION_SWITCH)) {
            add(TextViewHolder.UiModel(R.string.case_study_basic_setup_animation_duration_switch_2))
            add(LoadingIndicatorViewHolder.UiModel())
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<BasicSetupListItem>.addDeviceInfoSection() {
        if (addSectionHeader(Section.DEVICE_INFO)) {
            add(TextViewHolder.UiModel(R.string.case_study_basic_setup_device_info_2))
            add(SpaceViewHolder.UiModel())
        }
    }

    private fun MutableList<BasicSetupListItem>.addSectionHeader(section: Section) = (selectedSection == section).also { isExpanded ->
        add(SectionHeaderViewHolder.UiModel(section.titleResourceId, isExpanded))
    }

    private enum class Section(@StringRes val titleResourceId: Int) {
        HEADER(R.string.case_study_basic_setup_header_1),
        APP_INFO_BUTTON(R.string.case_study_basic_setup_app_info_button_1),
        DEVELOPER_OPTIONS_BUTTON(R.string.case_study_basic_setup_developer_options_button_1),
        FORCE_CRASH_BUTTON(R.string.case_study_basic_setup_force_crash_button_1),
        KEYLINE_OVERLAY_SWITCH(R.string.case_study_basic_setup_keyline_overlay_switch_1),
        ANIMATION_DURATION_SWITCH(R.string.case_study_basic_setup_animation_duration_switch_1),
        DEVICE_INFO(R.string.case_study_basic_setup_device_info_1);

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.titleResourceId == titleResourceId }
        }
    }
}