package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import org.koin.android.ext.android.inject

class BasicSetupFragment : InspirationDetailFragment<BasicSetupViewModel, ListItem>(R.string.case_study_basic_setup_title) {

    override val viewModel by inject<BasicSetupViewModel>()

    override fun createAdapter() = object : BaseAdapter<ListItem>() {}

    override fun getBeagleModules(): List<Module<*>> = listOf(
        AppInfoButtonModule(),
        AnimationDurationSwitchModule()
    )

    companion object {
        fun newInstance() = BasicSetupFragment()
    }
}