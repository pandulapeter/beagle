package com.pandulapeter.beagle.appDemo.feature.main.about

import com.pandulapeter.beagle.appDemo.BuildConfig
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentAboutBinding
import com.pandulapeter.beagle.appDemo.feature.main.MainChildFragment
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : MainChildFragment<FragmentAboutBinding, AboutViewModel>(R.layout.fragment_about, R.string.about_title) {

    override val viewModel by viewModel<AboutViewModel>()
    override val appBar get() = binding.appBar

    override fun getBeagleModules(): List<Module<*>> = listOf(
        TextModule(text = "Work in progress\nv${BuildConfig.VERSION_NAME}")
    )

    companion object {
        fun newInstance() = AboutFragment()
    }
}