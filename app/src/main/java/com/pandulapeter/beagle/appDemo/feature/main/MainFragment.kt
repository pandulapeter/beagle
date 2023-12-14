package com.pandulapeter.beagle.appDemo.feature.main

import android.os.Bundle
import android.view.View
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentMainBinding
import com.pandulapeter.beagle.appDemo.feature.main.about.AboutContainerFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesContainerFragment
import com.pandulapeter.beagle.appDemo.feature.main.playground.PlaygroundContainerFragment
import com.pandulapeter.beagle.appDemo.feature.main.setup.SetupContainerFragment
import com.pandulapeter.beagle.appDemo.feature.shared.BaseFragment
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.utils.consume

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            consume {
                when (item.itemId) {
                    R.id.setup -> childFragmentManager.handleReplace(addToBackStack = true, newInstance = SetupContainerFragment.Companion::newInstance)
                    R.id.examples -> childFragmentManager.handleReplace(addToBackStack = true, newInstance = ExamplesContainerFragment.Companion::newInstance)
                    R.id.playground -> childFragmentManager.handleReplace(addToBackStack = true, newInstance = PlaygroundContainerFragment.Companion::newInstance)
                    R.id.about -> childFragmentManager.handleReplace(addToBackStack = true, newInstance = AboutContainerFragment.Companion::newInstance)
                    else -> throw  IllegalArgumentException("Unsupported bottom navigation item.")
                }
            }
        }
        if (savedInstanceState == null && currentFragment == null) {
            binding.bottomNavigationView.selectedItemId = R.id.setup
        }
        binding.bottomNavigationView.setOnNavigationItemReselectedListener { onBackPressed() }
    }

    override fun onBackPressed() = if ((currentFragment?.childFragmentManager?.backStackEntryCount ?: 0) <= 1) false else super.onBackPressed()

    companion object {
        fun newInstance() = MainFragment()
    }
}