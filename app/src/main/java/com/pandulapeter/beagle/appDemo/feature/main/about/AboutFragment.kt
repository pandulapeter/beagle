package com.pandulapeter.beagle.appDemo.feature.main.about

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.BuildConfig
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.HeaderModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : ListFragment<AboutViewModel, ListItem>(R.string.about_title) {

    override val viewModel by viewModel<AboutViewModel>()

    override fun createAdapter() = BaseAdapter<ListItem>(viewModel.viewModelScope)

    override fun getBeagleModules(): List<Module<*>> = listOf(
        HeaderModule(
            title = getString(R.string.app_name),
            subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            text = "Built on ${BuildConfig.BUILD_DATE}"
        )
    )

    companion object {
        fun newInstance() = AboutFragment()
    }
}