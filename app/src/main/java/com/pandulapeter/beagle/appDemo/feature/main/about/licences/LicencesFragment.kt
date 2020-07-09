package com.pandulapeter.beagle.appDemo.feature.main.about.licences

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.about.licences.list.LicencesAdapter
import com.pandulapeter.beagle.appDemo.feature.main.about.licences.list.LicencesListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.utils.openUrl
import com.pandulapeter.beagle.common.contracts.module.Module
import org.koin.androidx.viewmodel.ext.android.viewModel

class LicencesFragment : ListFragment<LicencesViewModel, LicencesListItem>(R.string.licences_title) {

    override val viewModel by viewModel<LicencesViewModel>()

    override fun getBeagleModules() = emptyList<Module<*>>()

    override fun createAdapter() = LicencesAdapter(viewModel.viewModelScope, ::onLicenceSelected)

    private fun onLicenceSelected(url: String) = binding.recyclerView.openUrl(url)

    companion object {
        fun newInstance() = LicencesFragment()
    }
}