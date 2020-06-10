package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup

import android.os.Bundle
import com.google.android.material.transition.MaterialSharedAxis
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentBasicSetupBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseViewModelFragment
import org.koin.android.ext.android.inject

class BasicSetupFragment : BaseViewModelFragment<FragmentBasicSetupBinding, BasicSetupViewModel>(R.layout.fragment_basic_setup, false, R.string.case_study_basic_setup_title) {

    override val viewModel by inject<BasicSetupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    companion object {
        fun newInstance() = BasicSetupFragment()
    }
}