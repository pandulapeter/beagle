package com.pandulapeter.beagle.appDemo.feature.main.about

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentAboutBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseViewModelFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : BaseViewModelFragment<FragmentAboutBinding, AboutViewModel>(R.layout.fragment_about, true, R.string.about_title) {

    override val viewModel by viewModel<AboutViewModel>()

    companion object {
        fun newInstance() = AboutFragment()
    }
}