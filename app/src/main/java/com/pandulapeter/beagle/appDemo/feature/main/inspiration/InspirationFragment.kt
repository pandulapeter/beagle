package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.CaseStudy
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.AuthenticationFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.BasicSetupFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.FeatureTogglesFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationAdapter
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.utils.TransitionType
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.isContainerTransformSupported
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class InspirationFragment : ListFragment<InspirationViewModel, InspirationListItem>(R.string.inspiration_title) {

    override val viewModel by viewModel<InspirationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        refreshBeagle()
    }

    override fun createAdapter() = InspirationAdapter(viewModel.viewModelScope, ::onCaseStudySelected)

    override fun getBeagleModules() = mutableListOf<Module<*>>().apply {
        add(TextModule(text = getString(R.string.inspiration_beagle_text)))
        if (isContainerTransformSupported) {
            add(
                SwitchModule(
                    id = SHARED_ELEMENT_TRANSITIONS_SWITCH,
                    text = getString(R.string.inspiration_beagle_switch),
                    shouldBePersisted = true,
                    onValueChanged = {})
            )
        }
    }

    private fun onCaseStudySelected(caseStudy: CaseStudy, view: View) = when (caseStudy) {
        CaseStudy.BASIC_SETUP -> navigateTo(BasicSetupFragment.Companion::newInstance, view)
        CaseStudy.AUTHENTICATION -> navigateTo(AuthenticationFragment.Companion::newInstance, view)
        CaseStudy.FEATURE_TOGGLES -> navigateTo(FeatureTogglesFragment.Companion::newInstance, view)
        else -> binding.root.showSnackbar(caseStudy.title)
    }

    private inline fun <reified T : InspirationDetailFragment<*, *>> navigateTo(crossinline newInstance: () -> T, sharedElement: View) = parentFragment?.childFragmentManager?.handleReplace(
        addToBackStack = true,
        //TODO: Unpredictable, frequent glitches
        sharedElements = if (Beagle.find<SwitchModule>(SHARED_ELEMENT_TRANSITIONS_SWITCH)?.getCurrentValue(Beagle) == true) listOf(sharedElement) else emptyList(),
        transitionType = TransitionType.MODAL,
        newInstance = newInstance
    ) ?: Unit

    companion object {
        private const val SHARED_ELEMENT_TRANSITIONS_SWITCH = "sharedElementTransitions"

        fun newInstance() = InspirationFragment()
    }
}