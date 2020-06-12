package com.pandulapeter.beagle.appDemo.feature.main

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentContainerBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseFragment
import com.pandulapeter.beagle.appDemo.feature.shared.DestinationFragment
import com.pandulapeter.beagle.appDemo.utils.handleReplace

abstract class ContainerFragment(private val newInstance: () -> DestinationFragment<*, *>) : BaseFragment<FragmentContainerBinding>(R.layout.fragment_container) {

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null && currentFragment == null) {
            childFragmentManager.handleReplace(addToBackStack = true, newInstance = newInstance)
        }
    }
}