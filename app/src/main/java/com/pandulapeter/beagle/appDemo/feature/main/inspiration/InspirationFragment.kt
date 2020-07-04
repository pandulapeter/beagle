package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.CaseStudy
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.AuthenticationFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.FeatureTogglesFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationAdapter
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.NetworkRequestInterceptorFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.simpleSetup.SimpleSetupFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.staticData.StaticDataFragment
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.utils.TransitionType
import com.pandulapeter.beagle.appDemo.utils.createTextModule
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.contracts.module.Module
import org.koin.androidx.viewmodel.ext.android.viewModel

class InspirationFragment : ListFragment<InspirationViewModel, InspirationListItem>(R.string.inspiration_title) {

    override val viewModel by viewModel<InspirationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        refreshBeagle()
    }

    override fun createAdapter() = InspirationAdapter(viewModel.viewModelScope, ::onCaseStudySelected)

    override fun getBeagleModules() = listOf<Module<*>>(
        createTextModule(R.string.inspiration_beagle_text)
    )

    private fun onCaseStudySelected(caseStudy: CaseStudy, view: View) = when (caseStudy) {
        CaseStudy.SIMPLE_SETUP -> navigateTo(SimpleSetupFragment.Companion::newInstance, view)
        CaseStudy.STATIC_DATA -> navigateTo(StaticDataFragment.Companion::newInstance, view)
        CaseStudy.FEATURE_TOGGLES -> navigateTo(FeatureTogglesFragment.Companion::newInstance, view)
        CaseStudy.AUTHENTICATION -> navigateTo(AuthenticationFragment.Companion::newInstance, view)
        CaseStudy.NETWORK_REQUEST_INTERCEPTOR -> navigateTo(NetworkRequestInterceptorFragment.Companion::newInstance, view)
        else -> binding.root.showSnackbar(caseStudy.title)
    }

    private inline fun <reified T : InspirationDetailFragment<*, *>> navigateTo(crossinline newInstance: () -> T, sharedElement: View) = parentFragment?.childFragmentManager?.handleReplace(
        addToBackStack = true,
        //TODO: Unpredictable, frequent glitches: sharedElements =  listOf(sharedElement),
        transitionType = TransitionType.MODAL,
        newInstance = newInstance
    ) ?: Unit

    companion object {
        fun newInstance() = InspirationFragment()
    }
}