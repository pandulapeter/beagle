package com.pandulapeter.beagle.appDemo.feature.main.about.licences

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import org.koin.android.ext.android.inject

class LicencesFragment : ListFragment<LicencesViewModel, ListItem>(R.string.licences_title) {

    override val viewModel by inject<LicencesViewModel>()

    override fun getBeagleModules() = emptyList<Module<*>>()

    override fun createAdapter() = object : BaseAdapter<ListItem>(viewModel.viewModelScope) {}

    companion object {
        fun newInstance() = LicencesFragment()
    }
}