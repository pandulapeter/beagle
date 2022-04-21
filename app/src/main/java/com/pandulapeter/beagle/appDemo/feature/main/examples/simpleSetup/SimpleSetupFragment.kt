package com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.SimpleSetupAdapter
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.SimpleSetupListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SimpleSetupFragment : ExamplesDetailFragment<SimpleSetupViewModel, SimpleSetupListItem>(R.string.case_study_simple_setup_title) {

    override val viewModel by viewModel<SimpleSetupViewModel>()

    override fun createAdapter() = SimpleSetupAdapter(
        scope = viewModel.viewModelScope,
        onSectionHeaderSelected = viewModel::onSectionHeaderSelected
    )

    override fun getBeagleModules(): List<Module<*>> = mutableListOf<Module<*>>().apply {
        add(AppInfoButtonModule())
        add(DeveloperOptionsButtonModule())
        add(ForceCrashButtonModule())
        add(ScreenshotButtonModule())
        add(ScreenRecordingButtonModule())
        add(GalleryButtonModule())
        add(ScreenCaptureToolboxModule())
        add(KeylineOverlaySwitchModule())
        add(AnimationDurationSwitchModule(onValueChanged = { viewModel.refreshItems() }))
        add(LifecycleLogListModule())
        add(DeviceInfoModule())
        add(BugReportButtonModule())
    }

    companion object {
        fun newInstance() = SimpleSetupFragment()
    }
}