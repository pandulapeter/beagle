package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.BasicSetupAdapter
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.BasicSetupListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import org.koin.android.ext.android.inject

class BasicSetupFragment : InspirationDetailFragment<BasicSetupViewModel, BasicSetupListItem>(R.string.case_study_basic_setup_title) {

    override val viewModel by inject<BasicSetupViewModel>()

    override fun createAdapter() = BasicSetupAdapter()

    override fun getBeagleModules(): List<Module<*>> = listOf(
        AppInfoButtonModule(),
        AnimationDurationSwitchModule(onValueChanged = { viewModel.refreshItems() })
    )

    companion object {
        fun newInstance() = BasicSetupFragment()
    }
}