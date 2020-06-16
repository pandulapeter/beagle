package com.pandulapeter.beagle.appDemo.feature.main.setup

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.GithubButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.RadioButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class SetupViewModel : ListViewModel<SetupListItem>() {

    private val _items = MutableLiveData<List<SetupListItem>>()
    override val items: LiveData<List<SetupListItem>> = _items
    private var selectedUiVariant = UiVariant.ACTIVITY
        set(value) {
            field = value
            refreshItems()
        }

    init {
        refreshItems()
    }

    fun onRadioButtonSelected(position: Int) {
        UiVariant.fromResourceId((_items.value?.get(position) as? RadioButtonViewHolder.UiModel?)?.titleResourceId)?.let {
            selectedUiVariant = it
        }
    }

    private fun refreshItems() {
        _items.value = mutableListOf<SetupListItem>().apply {
            add(TextViewHolder.UiModel(R.string.setup_text_1))
            add(GithubButtonViewHolder.UiModel())
            add(TextViewHolder.UiModel(R.string.setup_text_2))
            addAll(UiVariant.values().map { uiVariant ->
                RadioButtonViewHolder.UiModel(uiVariant.titleResourceId, uiVariant == selectedUiVariant)
            })
            add(TextViewHolder.UiModel(R.string.setup_text_3))
            add(CodeSnippetViewHolder.UiModel("Beagle.initialize(this)"))
            add(TextViewHolder.UiModel(R.string.setup_text_4))
            add(TextViewHolder.UiModel(R.string.setup_text_5))
            add(CodeSnippetViewHolder.UiModel("Beagle.setModules(module1, module2, …)"))
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
}