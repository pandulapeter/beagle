package com.pandulapeter.beagle.appDemo.feature.main

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentMainBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseFragment

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    companion object {
        fun newInstance() = MainFragment()
    }
}