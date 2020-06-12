package com.pandulapeter.beagle.appDemo.feature.main.about

import com.pandulapeter.beagle.appDemo.feature.main.ContainerFragment

class AboutContainerFragment : ContainerFragment(AboutFragment.Companion::newInstance) {

    companion object {
        fun newInstance() = AboutContainerFragment()
    }
}