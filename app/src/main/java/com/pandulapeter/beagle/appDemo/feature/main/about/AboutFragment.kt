package com.pandulapeter.beagle.appDemo.feature.main.about

import com.pandulapeter.beagle.appDemo.BuildConfig
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentAboutBinding
import com.pandulapeter.beagle.appDemo.feature.shared.DestinationFragment
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.HeaderModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : DestinationFragment<FragmentAboutBinding, AboutViewModel>(R.layout.fragment_about, R.string.about_title) {

    override val viewModel by viewModel<AboutViewModel>()
    override val appBar get() = binding.appBar

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