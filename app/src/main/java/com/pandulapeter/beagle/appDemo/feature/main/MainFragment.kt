package com.pandulapeter.beagle.appDemo.feature.main

import android.os.Bundle
import android.view.View
import com.google.android.material.transition.Hold
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentMainBinding
import com.pandulapeter.beagle.appDemo.feature.main.about.AboutFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationFragment
import com.pandulapeter.beagle.appDemo.feature.main.playground.PlaygroundFragment
import com.pandulapeter.beagle.appDemo.feature.shared.BaseFragment
import com.pandulapeter.beagle.appDemo.utils.consume
import com.pandulapeter.beagle.appDemo.utils.handleReplace

class MainFragment : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            consume {
                when (item.itemId) {
                    R.id.inspiration -> childFragmentManager.handleReplace(newInstance = InspirationFragment.Companion::newInstance)
                    R.id.playground -> childFragmentManager.handleReplace(newInstance = PlaygroundFragment.Companion::newInstance)
                    R.id.about -> childFragmentManager.handleReplace(newInstance = AboutFragment.Companion::newInstance)
                    else -> throw  IllegalArgumentException("Unsupported bottom navigation item.")
                }
            }
        }
        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.inspiration
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}