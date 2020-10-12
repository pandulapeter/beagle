package com.pandulapeter.beagle.appDemo.feature.main.setup

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.about.AboutFragment
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupAdapter
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.utils.createTextModule
import com.pandulapeter.beagle.appDemo.utils.openUrl
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import com.pandulapeter.beagle.modules.DeviceInfoModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule
import com.pandulapeter.beagle.modules.ScreenshotButtonModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetupFragment : ListFragment<SetupViewModel, SetupListItem>(R.string.setup_title) {

    override val viewModel by viewModel<SetupViewModel>()
    private val spanCount get() = context?.resources?.getInteger(R.integer.setup_radio_button_span) ?: 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        refreshBeagle()
    }

    override fun createAdapter() = SetupAdapter(
        scope = viewModel.viewModelScope,
        onSectionHeaderSelected = viewModel::onSectionHeaderSelected,
        onGitHubButtonClicked = ::onGitHubButtonClicked,
        onRadioButtonSelected = viewModel::onRadioButtonSelected
    )

    override fun createLayoutManager() = GridLayoutManager(context, spanCount, RecyclerView.VERTICAL, false).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int) = if (viewModel.shouldBeFullSize(position)) spanCount else 1
        }
    }

    override fun getBeagleModules() = listOf(
        createTextModule(R.string.setup_debug_menu_text_1),
        DividerModule(id = "divider1"),
        AppInfoButtonModule(text = Text.ResourceId(R.string.setup_debug_menu_app_info_button)),
        ScreenshotButtonModule(text = Text.ResourceId(R.string.setup_debug_menu_screenshot_button), type = TextModule.Type.BUTTON),
        KeylineOverlaySwitchModule(text = { Text.ResourceId(R.string.setup_debug_menu_keyline_overlay_switch) }),
        listOf(
            RadioGroupOption(getString(R.string.setup_debug_menu_radio_group_option_1)),
            RadioGroupOption(getString(R.string.setup_debug_menu_radio_group_option_2)),
            RadioGroupOption(getString(R.string.setup_debug_menu_radio_group_option_3))
        ).let { radioGroupOptions ->
            SingleSelectionListModule(
                id = "radioGroup",
                title = getText(R.string.setup_debug_menu_radio_group),
                items = radioGroupOptions,
                isValuePersisted = true,
                initiallySelectedItemId = radioGroupOptions.last().id
            )
        },
        DeviceInfoModule(title = Text.ResourceId(R.string.setup_debug_menu_device_information)),
        DividerModule(id = "divider2"),
        createTextModule(R.string.setup_debug_menu_text_2)
    )

    private fun onGitHubButtonClicked() = binding.recyclerView.openUrl(AboutFragment.GITHUB_URL)

    private data class RadioGroupOption(
        val name: String,
    ) : BeagleListItemContract {

        override val title = Text.CharSequence(name)
    }

    companion object {
        fun newInstance() = SetupFragment()
    }
}