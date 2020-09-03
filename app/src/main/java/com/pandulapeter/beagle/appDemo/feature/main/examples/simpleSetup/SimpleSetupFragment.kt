package com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup

import android.os.Build
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
import com.pandulapeter.beagle.modules.GalleryButtonModule
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule
import com.pandulapeter.beagle.modules.LifecycleLogListModule
import com.pandulapeter.beagle.modules.ScreenCaptureToolboxModule
import com.pandulapeter.beagle.modules.ScreenRecordingButtonModule
import com.pandulapeter.beagle.modules.ScreenshotButtonModule
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            add(ScreenRecordingButtonModule())
        }
        add(GalleryButtonModule())
        add(ScreenCaptureToolboxModule())
        add(KeylineOverlaySwitchModule())
        add(AnimationDurationSwitchModule(onValueChanged = { viewModel.refreshItems() }))
        add(LifecycleLogListModule())
        add(DeviceInfoModule())
    }

    companion object {
        fun newInstance() = SimpleSetupFragment()
    }
}