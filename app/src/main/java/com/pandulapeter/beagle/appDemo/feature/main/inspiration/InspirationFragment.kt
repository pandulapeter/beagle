package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import android.os.Bundle
import android.view.View
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.CaseStudy
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.AuthenticationFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.BasicSetupFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.FeatureTogglesFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationAdapter
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationListItem
import com.pandulapeter.beagle.appDemo.utils.TransitionType
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.showToast
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class InspirationFragment : ListFragment<InspirationViewModel, InspirationListItem>(R.string.inspiration_title) {

    override val viewModel by viewModel<InspirationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        refreshBeagle()
    }

    override fun createAdapter() = InspirationAdapter(::onCaseStudySelected)

    override fun getBeagleModules(): List<Module<*>> = mutableListOf<Module<*>>().apply {
        add(TextModule(text = getText(R.string.inspiration_beagle_text_1)))
        add(LabelModule(title = getString(R.string.inspiration_beagle_label)))
        add(SwitchModule(text = getString(R.string.inspiration_beagle_switch), onValueChanged = {
            showToast(if (it) R.string.inspiration_beagle_switch_toast_on else R.string.inspiration_beagle_switch_toast_off)
        }))
        add(ButtonModule(text = getString(R.string.inspiration_beagle_button), onButtonPressed = {
            showToast(R.string.inspiration_beagle_button_toast)
        }))
        add(CheckBoxModule(text = getString(R.string.inspiration_beagle_check_box), onValueChanged = {
            showToast(if (it) R.string.inspiration_beagle_check_box_toast_on else R.string.inspiration_beagle_check_box_toast_off)
        }))
        add(listOf(
            RadioGroupOption(getString(R.string.inspiration_beagle_radio_group_option_1)),
            RadioGroupOption(getString(R.string.inspiration_beagle_radio_group_option_2)),
            RadioGroupOption(getString(R.string.inspiration_beagle_radio_group_option_3))
        ).let { radioGroupOptions ->
            SingleSelectionListModule(
                title = getString(R.string.inspiration_beagle_radio_group),
                items = radioGroupOptions,
                initiallySelectedItemId = radioGroupOptions.first().id,
                onSelectionChanged = { showToast(it?.title.orEmpty()) }
            )
        })
        add(TextModule(text = getText(R.string.inspiration_beagle_text_2)))
    }

    private fun onCaseStudySelected(caseStudy: CaseStudy, view: View) = when (caseStudy) {
        CaseStudy.BASIC_SETUP -> parentFragmentManager.handleReplace(
            addToBackStack = true,
            sharedElements = listOf(view),
            transitionType = TransitionType.MODAL,
            newInstance = BasicSetupFragment.Companion::newInstance
        )
        CaseStudy.AUTHENTICATION -> parentFragmentManager.handleReplace(
            addToBackStack = true,
            sharedElements = listOf(view),
            transitionType = TransitionType.MODAL,
            newInstance = AuthenticationFragment.Companion::newInstance
        )
        CaseStudy.FEATURE_TOGGLES -> parentFragmentManager.handleReplace(
            addToBackStack = true,
            sharedElements = listOf(view),
            transitionType = TransitionType.MODAL,
            newInstance = FeatureTogglesFragment.Companion::newInstance
        )
        else -> showToast("TODO: Open ${getString(caseStudy.title)} example")
    }

    private data class RadioGroupOption(
        override val title: String,
        override val id: String = title
    ) : BeagleListItemContract

    companion object {

        fun newInstance() = InspirationFragment()
    }
}