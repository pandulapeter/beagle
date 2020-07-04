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
import com.pandulapeter.beagle.appDemo.utils.createButtonModule
import com.pandulapeter.beagle.appDemo.utils.createCheckBoxModule
import com.pandulapeter.beagle.appDemo.utils.createLabelModule
import com.pandulapeter.beagle.appDemo.utils.createSwitchModule
import com.pandulapeter.beagle.appDemo.utils.createTextModule
import com.pandulapeter.beagle.appDemo.utils.openUrl
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
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
        createLabelModule(R.string.setup_debug_menu_label),
        createSwitchModule(R.string.setup_debug_menu_switch, onValueChanged = {}),
        createButtonModule(R.string.setup_debug_menu_button, onButtonPressed = {}),
        createCheckBoxModule(R.string.setup_debug_menu_check_box, onValueChanged = {}),
        listOf(
            RadioGroupOption(getString(R.string.setup_debug_menu_radio_group_option_1)),
            RadioGroupOption(getString(R.string.setup_debug_menu_radio_group_option_2)),
            RadioGroupOption(getString(R.string.setup_debug_menu_radio_group_option_3))
        ).let { radioGroupOptions ->
            SingleSelectionListModule(
                id = "radioGroup",
                title = getString(R.string.setup_debug_menu_radio_group),
                items = radioGroupOptions,
                initiallySelectedItemId = radioGroupOptions.first().id,
                onSelectionChanged = { }
            )
        },
        DividerModule(id = "divider2"),
        createTextModule(R.string.setup_debug_menu_text_2)
    )

    private fun onGitHubButtonClicked() = binding.recyclerView.openUrl(AboutFragment.GITHUB_URL)

    private data class RadioGroupOption(
        override val title: String,
        override val id: String = title
    ) : BeagleListItemContract

    companion object {
        fun newInstance() = SetupFragment()
    }
}