package com.pandulapeter.beagle.appDemo.feature.main.setup

import com.pandulapeter.beagle.appDemo.feature.main.ContainerFragment

class SetupContainerFragment : ContainerFragment(SetupFragment.Companion::newInstance) {

    companion object {
        fun newInstance() = SetupContainerFragment()
    }
}