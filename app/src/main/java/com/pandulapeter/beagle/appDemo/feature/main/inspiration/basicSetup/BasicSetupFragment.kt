package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.BuildConfig
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.BasicSetupAdapter
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.BasicSetupListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import com.pandulapeter.beagle.modules.DeveloperOptionsButtonModule
import com.pandulapeter.beagle.modules.DeviceInfoModule
import com.pandulapeter.beagle.modules.ForceCrashButtonModule
import com.pandulapeter.beagle.modules.HeaderModule
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class BasicSetupFragment : InspirationDetailFragment<BasicSetupViewModel, BasicSetupListItem>(R.string.case_study_basic_setup_title) {

    override val viewModel by viewModel<BasicSetupViewModel>()

    override fun createAdapter() = BasicSetupAdapter(
        scope = viewModel.viewModelScope,
        onSectionHeaderSelected = viewModel::onSectionHeaderSelected
    )

    override fun getBeagleModules(): List<Module<*>> = listOf(
        HeaderModule(
            title = getString(R.string.app_name),
            subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            text = "Built on ${BuildConfig.BUILD_DATE}"
        ),
        AppInfoButtonModule(),
        DeveloperOptionsButtonModule(),
        ForceCrashButtonModule(),
        KeylineOverlaySwitchModule(),
        AnimationDurationSwitchModule(onValueChanged = { viewModel.refreshItems() }),
        DeviceInfoModule()
    )

    companion object {
        fun newInstance() = BasicSetupFragment()
    }
}