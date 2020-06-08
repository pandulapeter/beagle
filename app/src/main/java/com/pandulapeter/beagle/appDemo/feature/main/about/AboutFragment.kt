package com.pandulapeter.beagle.appDemo.feature.main.about

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentHomeBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseFragment

class AboutFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_about, true, R.string.about_title) {

    companion object {
        fun newInstance() = AboutFragment()
    }
}