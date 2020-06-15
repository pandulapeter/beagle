package com.pandulapeter.beagle.appDemo.feature.main.setup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.ButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class SetupViewModel : ViewModel() {

    private val _items = MutableLiveData<List<InspirationListItem>>()
    val items: LiveData<List<InspirationListItem>> = _items

    fun refreshItems(shouldShowTutorial: Boolean) {
        _items.value = mutableListOf<InspirationListItem>().apply {
            add(TextViewHolder.UiModel(R.string.inspiration_welcome_hint_1))
            add(ButtonViewHolder.UiModel())
            add(TextViewHolder.UiModel(R.string.inspiration_welcome_hint_2))
            add(CodeSnippetViewHolder.UiModel("Beagle.initialize(this)"))
            add(TextViewHolder.UiModel(R.string.inspiration_welcome_hint_3))
            add(TextViewHolder.UiModel(R.string.inspiration_welcome_hint_4))
            add(CodeSnippetViewHolder.UiModel("Beagle.setModules(module1, module2, …)"))
        }
    }
}