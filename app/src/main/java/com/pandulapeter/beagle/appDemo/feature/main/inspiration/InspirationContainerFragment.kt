package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import com.pandulapeter.beagle.appDemo.feature.main.ContainerFragment

class InspirationContainerFragment : ContainerFragment(InspirationFragment.Companion::newInstance) {

    companion object {
        fun newInstance() = InspirationContainerFragment()
    }
}