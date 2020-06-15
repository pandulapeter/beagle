package com.pandulapeter.beagle.appDemo.feature.main.setup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupAdapter
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListFragment
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.appDemo.utils.showToast
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetupFragment : ListFragment<SetupViewModel, SetupListItem>(R.string.setup_title) {

    override val viewModel by viewModel<SetupViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        refreshBeagle()
    }

    override fun createAdapter() = SetupAdapter(::onGitHubButtonClicked)

    override fun getBeagleModules() = listOf(
        TextModule(text = getText(R.string.setup_beagle_text_1)),
        LabelModule(title = getString(R.string.setup_beagle_label)),
        SwitchModule(text = getString(R.string.setup_beagle_switch), onValueChanged = {
            showToast(if (it) R.string.setup_beagle_switch_toast_on else R.string.setup_beagle_switch_toast_off)
        }),
        ButtonModule(text = getString(R.string.setup_beagle_button), onButtonPressed = {
            showToast(R.string.setup_beagle_button_toast)
        }),
        CheckBoxModule(text = getString(R.string.setup_beagle_check_box), onValueChanged = {
            showToast(if (it) R.string.setup_beagle_check_box_toast_on else R.string.setup_beagle_check_box_toast_off)
        }),
        listOf(
            RadioGroupOption(getString(R.string.setup_beagle_radio_group_option_1)),
            RadioGroupOption(getString(R.string.setup_beagle_radio_group_option_2)),
            RadioGroupOption(getString(R.string.setup_beagle_radio_group_option_3))
        ).let { radioGroupOptions ->
            SingleSelectionListModule(
                title = getString(R.string.setup_beagle_radio_group),
                items = radioGroupOptions,
                initiallySelectedItemId = radioGroupOptions.first().id,
                onSelectionChanged = { showToast(it?.title.orEmpty()) }
            )
        },
        TextModule(text = getText(R.string.setup_beagle_text_2))
    )

    private fun onGitHubButtonClicked() = Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL)).let { intent ->
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
        private const val GITHUB_URL = "https://github.com/pandulapeter/beagle"

        fun newInstance() = SetupFragment()
    }
}