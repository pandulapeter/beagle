package com.pandulapeter.beagle.appDemo.feature.main.examples.analytics

import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list.AnalyticsAdapter
import com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list.AnalyticsListItem
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.LogListModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class AnalyticsFragment : ExamplesDetailFragment<AnalyticsViewModel, AnalyticsListItem>(R.string.case_study_analytics_title) {

    override val viewModel by viewModel<AnalyticsViewModel>()
    private val spanCount get() = context?.resources?.getInteger(R.integer.analytics_check_box_span) ?: 1

    override fun createAdapter() = AnalyticsAdapter(
        scope = viewModel.viewModelScope,
        onSwitchToggled = viewModel::onSwitchToggled,
        onClearButtonPressed = ::onClearButtonPressed
    )

    override fun createLayoutManager() = GridLayoutManager(context, spanCount, RecyclerView.VERTICAL, false).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = if (viewModel.shouldBeFullSize(position)) spanCount else 1
        }
    }

    override fun getBeagleModules(): List<Module<*>> = listOf(
        LogListModule(
            title = getText(R.string.case_study_analytics_module_title),
            isExpandedInitially = true,
            maxItemCount = 20,
            label = LOG_TAG
        )
    )

    private fun onClearButtonPressed() {
        Beagle.clearLogs(LOG_TAG)
        binding.recyclerView.showSnackbar(R.string.case_study_analytics_logs_cleared)
    }

    companion object {
        const val LOG_TAG = "analytics"

        fun newInstance() = AnalyticsFragment()
    }
}