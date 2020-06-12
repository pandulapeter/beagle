package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailViewModel
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.BasicSetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.LoadingIndicatorViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class BasicSetupViewModel : InspirationDetailViewModel<BasicSetupListItem>() {

    private val _items = MutableLiveData<List<BasicSetupListItem>>()
    override val items: LiveData<List<BasicSetupListItem>> = _items

    init {
        refreshItems()
    }

    fun refreshItems() {
        _items.value = listOf(
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_1),
            CodeSnippetViewHolder.UiModel(
                "Beagle.setModules(\n" +
                        "    AppInfoButtonModule(),\n" +
                        "    AnimationDurationSwitchModule()\n" +
                        ")"
            ),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_2),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_3),
            TextViewHolder.UiModel(R.string.case_study_basic_setup_text_4),
            LoadingIndicatorViewHolder.UiModel()
        )
    }
}