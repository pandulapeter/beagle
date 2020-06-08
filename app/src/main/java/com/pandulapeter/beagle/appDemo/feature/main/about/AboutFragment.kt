package com.pandulapeter.beagle.appDemo.feature.main.about

import android.os.Bundle
import android.view.View
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.BuildConfig
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentAboutBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseViewModelFragment
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : BaseViewModelFragment<FragmentAboutBinding, AboutViewModel>(R.layout.fragment_about, true, R.string.about_title) {

    override val viewModel by viewModel<AboutViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBeagle()
    }

    private fun setupBeagle() = Beagle.setModules(TextModule(text = "Work in progress\nv${BuildConfig.VERSION_NAME}"))

    companion object {
        fun newInstance() = AboutFragment()
    }
}