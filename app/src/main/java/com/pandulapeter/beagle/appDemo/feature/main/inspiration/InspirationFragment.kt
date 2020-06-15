package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.CaseStudy
import com.pandulapeter.beagle.appDemo.databinding.FragmentInspirationBinding
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.AuthenticationFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.BasicSetupFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.FeatureTogglesFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.DestinationFragment
import com.pandulapeter.beagle.appDemo.utils.TransitionType
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.appDemo.utils.showToast
import com.pandulapeter.beagle.appDemo.utils.waitForLayout
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class InspirationFragment : DestinationFragment<FragmentInspirationBinding, InspirationViewModel>(R.layout.fragment_inspiration, R.string.inspiration_title) {

    override val viewModel by viewModel<InspirationViewModel>()
    override val appBar get() = binding.appBar
    private val textModule1 by lazy { TextModule(text = getText(R.string.inspiration_beagle_text_1)) }
    private val labelModule by lazy { LabelModule(title = getString(R.string.inspiration_beagle_label)) }
    private val switchModule by lazy {
        SwitchModule(text = getString(R.string.inspiration_beagle_switch), onValueChanged = {
            showToast(if (it) R.string.inspiration_beagle_switch_toast_on else R.string.inspiration_beagle_switch_toast_off)
        })
    }
    private val buttonModule by lazy {
        ButtonModule(text = getString(R.string.inspiration_beagle_button), onButtonPressed = {
            showToast(R.string.inspiration_beagle_button_toast)
        })
    }
    private val checkBoxModule by lazy {
        CheckBoxModule(text = getString(R.string.inspiration_beagle_check_box), onValueChanged = {
            showToast(if (it) R.string.inspiration_beagle_check_box_toast_on else R.string.inspiration_beagle_check_box_toast_off)
        })
    }
    private val radioGroupModule by lazy {
        listOf(
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
        }
    }
    private val textModule2 by lazy { TextModule(text = getText(R.string.inspiration_beagle_text_2)) }
    private val tutorialSwitchModule by lazy {
        SwitchModule(
            id = TUTORIAL_SWITCH_ID, // Persisting modules only works if we have a constant ID.
            initialValue = true,
            shouldBePersisted = true,
            text = getString(R.string.inspiration_beagle_tutorial_switch),
            onValueChanged = ::onShouldShowTutorialToggled
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        setupRecyclerView()
        onShouldShowTutorialToggled(tutorialSwitchModule.getCurrentValue(Beagle) == true)
    }

    override fun getBeagleModules(): List<Module<*>> = mutableListOf<Module<*>>().apply {
        add(textModule1)
        if (tutorialSwitchModule.getCurrentValue(Beagle) == true) {
            add(labelModule)
            add(switchModule)
            add(buttonModule)
            add(checkBoxModule)
            add(radioGroupModule)
            add(textModule2)
        }
        add(tutorialSwitchModule)
    }

    private fun setupRecyclerView() {
        val inspirationAdapter = InspirationAdapter(::onCaseStudySelected, ::onGitHubButtonClicked)
        binding.recyclerView.run {
            adapter = inspirationAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            waitForLayout { startPostponedEnterTransition() }
        }
        viewModel.items.observeForever(inspirationAdapter::submitList)
    }

    private fun onShouldShowTutorialToggled(shouldShowTutorial: Boolean) {
        viewModel.refreshItems(shouldShowTutorial)
        refreshBeagle()
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

    private fun onGitHubButtonClicked() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL))
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            binding.recyclerView.showSnackbar(R.string.inspiration_welcome_browser_not_found)
        }
    }

    private data class RadioGroupOption(
        override val title: String,
        override val id: String = title
    ) : BeagleListItemContract

    companion object {
        private const val TUTORIAL_SWITCH_ID = "tutorialSwitch"
        private const val GITHUB_URL = "https://github.com/pandulapeter/beagle"

        fun newInstance() = InspirationFragment()
    }
}