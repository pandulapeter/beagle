package com.pandulapeter.beagle.appDemo.feature.main.examples.crashLogging

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.ForceCrashButtonModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class CrashLoggingFragment : ExamplesDetailFragment<CrashLoggingViewModel, ListItem>(R.string.case_study_crash_logging_title) {

    override val viewModel by viewModel<CrashLoggingViewModel>()

    override fun createAdapter() = object : BaseAdapter<ListItem>(
        scope = viewModel.viewModelScope
    ) {}

    override fun getBeagleModules(): List<Module<*>> = listOf(
        TextModule("Coming soon"),
        ForceCrashButtonModule(type = TextModule.Type.BUTTON)
    )

    companion object {
        fun newInstance() = CrashLoggingFragment()
    }
}