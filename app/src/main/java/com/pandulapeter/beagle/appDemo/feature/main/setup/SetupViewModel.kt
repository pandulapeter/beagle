package com.pandulapeter.beagle.appDemo.feature.main.setup

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.BuildConfig
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.GithubButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.RadioButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SectionHeaderViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.SpaceViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import kotlin.properties.Delegates

class SetupViewModel : ListViewModel<SetupListItem>() {

    private val _items = MutableLiveData<List<SetupListItem>>()
    override val items: LiveData<List<SetupListItem>> = _items
    private var selectedUiVariant by Delegates.observable(UiVariant.ACTIVITY) { _, _, _ -> refreshItems() }
    private var selectedSection by Delegates.observable<Section?>(null) { _, _, _ -> refreshItems() }

    init {
        refreshItems()
    }

    fun onRadioButtonSelected(uiModel: RadioButtonViewHolder.UiModel) {
        UiVariant.fromResourceId(uiModel.titleResourceId)?.let {
            selectedUiVariant = it
        }
    }

    fun onSectionHeaderSelected(uiModel: SectionHeaderViewHolder.UiModel) {
        Section.fromResourceId(uiModel.titleResourceId).let {
            selectedSection = if (it == selectedSection) null else it
        }
    }

    fun shouldBeFullSize(position: Int) = _items.value?.get(position) !is RadioButtonViewHolder.UiModel

    private fun refreshItems() {
        _items.value = mutableListOf<SetupListItem>().apply {
            addWelcomeSection()
            addInitializationSection()
            addModuleConfigurationSection()
            addCustomizationSection()
            addLocalizationSection()
            addTroubleshootingSection()
        }
    }

    private fun MutableList<SetupListItem>.addWelcomeSection() {
        add(TextViewHolder.UiModel(R.string.setup_welcome_1))
        add(GithubButtonViewHolder.UiModel())
        add(TextViewHolder.UiModel(R.string.setup_welcome_2))
    }

    private fun MutableList<SetupListItem>.addInitializationSection() = addSection(Section.INITIALIZATION) {
        add(TextViewHolder.UiModel(R.string.setup_initialization_2))
        add(
            CodeSnippetViewHolder.UiModel(
                "allprojects {\n" +
                        "    repositories {\n" +
                        "        …\n" +
                        "        maven { url \"https://jitpack.io\" }\n" +
                        "    }\n" +
                        "}"
            )
        )
        add(TextViewHolder.UiModel(R.string.setup_initialization_3))
        add(SpaceViewHolder.UiModel())
        addAll(UiVariant.values().map { uiVariant ->
            RadioButtonViewHolder.UiModel(uiVariant.titleResourceId, uiVariant == selectedUiVariant)
        })
        add(TextViewHolder.UiModel(R.string.setup_initialization_4))
        add(
            CodeSnippetViewHolder.UiModel(
                id = "codeSnippet_gradle",
                codeSnippet = "dependencies {\n" +
                        "    …\n" +
                        "    def beagleVersion = \"${BuildConfig.BEAGLE_VERSION}\" // Check the GitHub repository for the latest version\n" +
                        "    debugImplementation \"com.github.pandulapeter.beagle:ui-${
                            when (selectedUiVariant) {
                                UiVariant.ACTIVITY -> "activity"
                                UiVariant.BOTTOM_SHEET -> "bottom-sheet"
                                UiVariant.DIALOG -> "dialog"
                                UiVariant.DRAWER -> "drawer"
                                UiVariant.VIEW -> "view"
                            }
                        }:\$beagleVersion\"\n" +
                        "    releaseImplementation \"com.github.pandulapeter.beagle:noop:\$beagleVersion\"\n" +
                        "}"
            )
        )
        add(TextViewHolder.UiModel(R.string.setup_initialization_5))
        add(CodeSnippetViewHolder.UiModel("Beagle.initialize(this)"))
        add(TextViewHolder.UiModel(R.string.setup_initialization_6))
    }

    private fun MutableList<SetupListItem>.addModuleConfigurationSection() = addSection(Section.MODULE_CONFIGURATION) {
        add(TextViewHolder.UiModel(R.string.setup_module_configuration_2))
        add(CodeSnippetViewHolder.UiModel("Beagle.set(module1, module2, …)"))
        add(TextViewHolder.UiModel(R.string.setup_module_configuration_3))
        add(
            CodeSnippetViewHolder.UiModel(
                "Beagle.add(\n" +
                        "    module1, module2, …,\n" +
                        "    placement =  …, // Optional\n" +
                        "    lifecycleOwner =  … // Optional\n" +
                        ")"
            )
        )
        add(TextViewHolder.UiModel(R.string.setup_module_configuration_4))
        add(CodeSnippetViewHolder.UiModel("Beagle.remove(moduleId1, moduleId2, ...)"))
        add(TextViewHolder.UiModel(R.string.setup_module_configuration_5))
        add(CodeSnippetViewHolder.UiModel("Beagle.find<ModuleType>(moduleId)"))
        add(TextViewHolder.UiModel(R.string.setup_module_configuration_6))
    }

    private fun MutableList<SetupListItem>.addCustomizationSection() = addSection(Section.CUSTOMIZATION) {
        add(TextViewHolder.UiModel(R.string.setup_customization_2))
        add(
            CodeSnippetViewHolder.UiModel(
                "Beagle.initialize(\n" +
                        "    application = …,\n" +
                        "    appearance = Appearance(…),\n" +
                        "    behavior = Behavior(…)\n" +
                        ")"
            )
        )
        add(TextViewHolder.UiModel(R.string.setup_customization_3))
    }

    private fun MutableList<SetupListItem>.addLocalizationSection() = addSection(Section.LOCALIZATION) {
        add(TextViewHolder.UiModel(R.string.setup_localization_2))
    }

    private fun MutableList<SetupListItem>.addTroubleshootingSection() = addSection(Section.TROUBLESHOOTING) {
        add(TextViewHolder.UiModel(R.string.setup_troubleshooting_2))
    }

    private fun MutableList<SetupListItem>.addSection(section: Section, action: MutableList<SetupListItem>.() -> Unit) = (selectedSection == section).also { isExpanded ->
        add(SectionHeaderViewHolder.UiModel(section.titleResourceId, isExpanded))
        if (isExpanded) {
            action()
            add(SpaceViewHolder.UiModel())
        }
    }

    private enum class UiVariant(@StringRes val titleResourceId: Int) {
        ACTIVITY(R.string.setup_variant_activity),
        BOTTOM_SHEET(R.string.setup_variant_bottom_sheet),
        DIALOG(R.string.setup_variant_dialog),
        DRAWER(R.string.setup_variant_drawer),
        VIEW(R.string.setup_variant_view);

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.titleResourceId == titleResourceId }
        }
    }

    private enum class Section(@StringRes val titleResourceId: Int) {
        INITIALIZATION(R.string.setup_initialization_1),
        MODULE_CONFIGURATION(R.string.setup_module_configuration_1),
        CUSTOMIZATION(R.string.setup_customization_1),
        LOCALIZATION(R.string.setup_localization_1),
        TROUBLESHOOTING(R.string.setup_troubleshooting_1);

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = values().firstOrNull { it.titleResourceId == titleResourceId }
        }
    }
}