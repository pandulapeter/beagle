package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles

import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.TextModule
import org.koin.android.ext.android.inject

class FeatureTogglesFragment : InspirationDetailFragment<FeatureTogglesViewModel, ListItem>(R.string.case_study_feature_toggles_title) {

    override val viewModel by inject<FeatureTogglesViewModel>()

    override fun createAdapter() = object : BaseAdapter<ListItem>() {}

    override fun getBeagleModules(): List<Module<*>> = listOf(
        TextModule(text = "TODO: Add switches, radio groups, checkbox groups.")
    )

    companion object {
        fun newInstance() = FeatureTogglesFragment()
    }
}