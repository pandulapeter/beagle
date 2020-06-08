package com.pandulapeter.beagle.appDemo.feature.main.home

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentHomeBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home, true, R.string.home_title) {

    companion object {
        fun newInstance() = HomeFragment()
    }
}