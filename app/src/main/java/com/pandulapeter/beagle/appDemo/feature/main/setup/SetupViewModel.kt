package com.pandulapeter.beagle.appDemo.feature.main.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.ButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class SetupViewModel : ListViewModel<SetupListItem>() {

    override val items: LiveData<List<SetupListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.inspiration_welcome_hint_1),
            ButtonViewHolder.UiModel(),
            TextViewHolder.UiModel(R.string.inspiration_welcome_hint_2),
            CodeSnippetViewHolder.UiModel("Beagle.initialize(this)"),
            TextViewHolder.UiModel(R.string.inspiration_welcome_hint_3),
            TextViewHolder.UiModel(R.string.inspiration_welcome_hint_4),
            CodeSnippetViewHolder.UiModel("Beagle.setModules(module1, module2, …)")
        )
    )
}