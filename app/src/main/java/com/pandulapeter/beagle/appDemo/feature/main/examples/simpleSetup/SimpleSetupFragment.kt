package com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.SimpleSetupAdapter
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.SimpleSetupListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule
import com.pandulapeter.beagle.modules.DeviceInfoModule
import com.pandulapeter.beagle.modules.ForceCrashButtonModule
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule
import com.pandulapeter.beagle.modules.ScreenshotButtonModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class SimpleSetupFragment : ExamplesDetailFragment<SimpleSetupViewModel, SimpleSetupListItem>(R.string.case_study_simple_setup_title) {

    override val viewModel by viewModel<SimpleSetupViewModel>()

    override fun createAdapter() = SimpleSetupAdapter(
        scope = viewModel.viewModelScope,
        onSectionHeaderSelected = viewModel::onSectionHeaderSelected
    )

    override fun getBeagleModules(): List<Module<*>> = listOf(
        AppInfoButtonModule(),
        DeveloperOptionsButtonModule(),
        ForceCrashButtonModule(),
        ScreenshotButtonModule(),
        KeylineOverlaySwitchModule(),
        AnimationDurationSwitchModule(onValueChanged = { viewModel.refreshItems() }),
        DeviceInfoModule()
    )

    companion object {
        fun newInstance() = SimpleSetupFragment()
    }
}