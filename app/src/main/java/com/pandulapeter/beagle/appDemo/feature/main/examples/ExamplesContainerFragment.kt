package com.pandulapeter.beagle.appDemo.feature.main.examples

import com.pandulapeter.beagle.appDemo.feature.main.ContainerFragment

class ExamplesContainerFragment : ContainerFragment(ExamplesFragment.Companion::newInstance) {

    companion object {
        fun newInstance() = ExamplesContainerFragment()
    }
}