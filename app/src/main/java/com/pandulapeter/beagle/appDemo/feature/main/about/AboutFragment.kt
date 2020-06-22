package com.pandulapeter.beagle.appDemo.feature.main.about

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.BuildConfig
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutAdapter
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.utils.openUrl
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.HeaderModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : ListFragment<AboutViewModel, AboutListItem>(R.string.about_title) {

    override val viewModel by viewModel<AboutViewModel>()

    override fun createAdapter() = AboutAdapter(viewModel.viewModelScope) { uiModel ->
        when (uiModel.textResourceId) {
            R.string.about_github -> binding.recyclerView.openUrl(GITHUB_URL)
            R.string.about_google_play,
            R.string.about_share,
            R.string.about_contact,
            R.string.about_donate,
            R.string.about_open_source -> binding.recyclerView.showSnackbar(R.string.coming_soon)
        }
    }

    override fun getBeagleModules(): List<Module<*>> = listOf(
        HeaderModule(
            title = getString(R.string.app_name),
            subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            text = "Built on ${BuildConfig.BUILD_DATE}"
        )
    )

    companion object {
        const val GITHUB_URL = "https://github.com/pandulapeter/beagle"

        fun newInstance() = AboutFragment()
    }
}