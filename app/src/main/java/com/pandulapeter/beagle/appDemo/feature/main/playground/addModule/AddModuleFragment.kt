package com.pandulapeter.beagle.appDemo.feature.main.playground.addModule

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.ModuleWrapper
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.AddModuleAdapter
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.AddModuleListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.ModuleViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddModuleFragment : ListFragment<AddModuleViewModel, AddModuleListItem>(R.string.add_module_title) {

    override val viewModel by viewModel<AddModuleViewModel>()

    override fun createAdapter() = AddModuleAdapter(viewModel.viewModelScope, ::onModuleSelected)

    override fun getBeagleModules() = emptyList<Module<*>>()

    private fun onModuleSelected(module: ModuleViewHolder.UiModel) {
        viewModel.onModuleSelected(
            ModuleWrapper(
                titleResourceId = module.titleResourceId,
                module = TextModule(text = "Hello") //TODO
            )
        )
        activity?.onBackPressed()
    }

    companion object {
        fun newInstance() = AddModuleFragment()
    }
}