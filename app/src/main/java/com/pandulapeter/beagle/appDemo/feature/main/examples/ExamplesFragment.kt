package com.pandulapeter.beagle.appDemo.feature.main.examples

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.model.CaseStudy
import com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.AnalyticsFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.AuthenticationFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureFlags.FeatureFlagsFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.list.ExamplesAdapter
import com.pandulapeter.beagle.appDemo.feature.main.examples.list.ExamplesListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.MockDataGeneratorFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.NetworkRequestInterceptorFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.overlay.OverlayFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.SimpleSetupFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.staticData.StaticDataFragment
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.utils.TransitionType
import com.pandulapeter.beagle.appDemo.utils.createTextModule
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExamplesFragment : ListFragment<ExamplesViewModel, ExamplesListItem>(R.string.examples_title) {

    override val viewModel by viewModel<ExamplesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        refreshBeagle()
    }

    override fun createAdapter() = ExamplesAdapter(viewModel.viewModelScope, ::onCaseStudySelected)

    override fun getBeagleModules() = mutableListOf<Module<*>>().apply {
        add(createTextModule(R.string.examples_beagle_text_1))
        // Opening Fragments within a backgrounded Activity is not a great idea.
        if (!requireContext().packageName.contains("activity", ignoreCase = true)) {
            add(createTextModule(R.string.examples_beagle_text_2))
            add(PaddingModule())
            addAll(CaseStudy.values().filter { it.isReady }.map { caseStudy ->
                TextModule(
                    id = caseStudy.id,
                    text = "• ${getString(caseStudy.title)}",
                    onItemSelected = { onCaseStudySelected(caseStudy) }
                )
            })
        }
    }

    private fun onCaseStudySelected(caseStudy: CaseStudy) = when (caseStudy) {
        CaseStudy.SIMPLE_SETUP -> navigateTo(SimpleSetupFragment.Companion::newInstance)
        CaseStudy.STATIC_DATA -> navigateTo(StaticDataFragment.Companion::newInstance)
        CaseStudy.FEATURE_FLAGS -> navigateTo(FeatureFlagsFragment.Companion::newInstance)
        CaseStudy.NETWORK_REQUEST_INTERCEPTOR -> navigateTo(NetworkRequestInterceptorFragment.Companion::newInstance)
        CaseStudy.ANALYTICS -> navigateTo(AnalyticsFragment.Companion::newInstance)
        CaseStudy.AUTHENTICATION -> navigateTo(AuthenticationFragment.Companion::newInstance)
        CaseStudy.MOCK_DATA_GENERATOR -> navigateTo(MockDataGeneratorFragment.Companion::newInstance)
        CaseStudy.OVERLAY -> navigateTo(OverlayFragment.Companion::newInstance)
        else -> binding.root.showSnackbar(caseStudy.title)
    }

    private inline fun <reified T : ExamplesDetailFragment<*, *>> navigateTo(crossinline newInstance: () -> T) = parentFragment?.childFragmentManager?.handleReplace(
        addToBackStack = true,
        transitionType = TransitionType.MODAL,
        newInstance = newInstance
    ) ?: Unit

    companion object {
        fun newInstance() = ExamplesFragment()
    }
}