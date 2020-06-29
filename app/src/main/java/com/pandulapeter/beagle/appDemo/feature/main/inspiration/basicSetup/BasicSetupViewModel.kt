package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.BasicSetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.LoadingIndicatorViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class BasicSetupViewModel : ListViewModel<BasicSetupListItem>() {

    private val _items = MutableLiveData<List<BasicSetupListItem>>()
    override val items: LiveData<List<BasicSetupListItem>> = _items

    init {
        refreshItems()
    }

    fun refreshItems() {
        _items.value = listOf(
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_1),
            CodeSnippetViewHolder.UiModel(
                "Beagle.set(\n" +
                        "    HeaderModule(\n" +
                        "        title = headerTitle,\n" +
                        "        subtitle = subtitle,\n" +
                        "        text = headerText\n" +
                        "    ),\n" +
                        "    AppInfoButtonModule(),\n" +
                        "    ForceCrashButtonModule(),\n" +
                        "    KeylineOverlaySwitchModule(),\n" +
                        "    AnimationDurationSwitchModule()\n" +
                        "    DeviceInformationModule()\n" +
                        ")"
            ),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_2),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_3),
            CodeSnippetViewHolder.UiModel(
                "val headerTitle = getString(R.string.app_name)\n" +
                        "val headerSubitle = \"v\${BuildConfig.VERSION_NAME} (\${BuildConfig.VERSION_CODE})\"\n" +
                        "val headerText = \"Built on \${BuildConfig.BUILD_DATE}\""
            ),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_4),
            CodeSnippetViewHolder.UiModel(
                "android {\n" +
                        "    …\n" +
                        "    defaultConfig {\n" +
                        "        …\n" +
                        "        buildConfigField(\"String\", \"BUILD_DATE\", \"\\\"\${new Date ().format(\"yyyy.MM.dd\")}\\\"\")\n" +
                        "    }\n" +
                        "}"
            ),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_5),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_6),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_7),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_8),
            LoadingIndicatorViewHolder.UiModel(),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_9)
        )
    }
}