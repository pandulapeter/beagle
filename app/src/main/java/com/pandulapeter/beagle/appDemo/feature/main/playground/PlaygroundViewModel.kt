package com.pandulapeter.beagle.appDemo.feature.main.playground

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.ModuleRepository
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.AddModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.GenerateCodeViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.ModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class PlaygroundViewModel(val moduleRepository: ModuleRepository) : ListViewModel<PlaygroundListItem>() {

    private val _items = MutableLiveData<List<PlaygroundListItem>>()
    override val items: LiveData<List<PlaygroundListItem>> = _items
    val modules get() = moduleRepository.modules

    fun refreshModules() {
        _items.value = mutableListOf<PlaygroundListItem>().apply {
            add(TextViewHolder.UiModel(R.string.playground_description))
            addAll(moduleRepository.modules.map { ModuleViewHolder.UiModel(it) })
            add(AddModuleViewHolder.UiModel())
            add(GenerateCodeViewHolder.UiModel())
        }
    }

    fun onModuleRemoved(position: Int) {
        (_items.value?.get(position) as? ModuleViewHolder.UiModel?)?.moduleWrapper?.id?.let {
            moduleRepository.removeModule(it)
        }
    }

    fun onModulesSwapped(oldPosition: Int, newPosition: Int) = moduleRepository.onModulesSwapped(oldPosition - FIRST_MODULE_OFFSET, newPosition - FIRST_MODULE_OFFSET)

    companion object {
        private const val FIRST_MODULE_OFFSET = 1
    }
}