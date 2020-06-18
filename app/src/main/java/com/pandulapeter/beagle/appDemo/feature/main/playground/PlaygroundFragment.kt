package com.pandulapeter.beagle.appDemo.feature.main.playground

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundAdapter
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.DeviceInfoModule
import com.pandulapeter.beagle.modules.HeaderModule
import com.pandulapeter.beagle.modules.ItemListModule
import com.pandulapeter.beagle.modules.KeyValueListModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import kotlinx.android.synthetic.main.item_playground_module.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaygroundFragment : ListFragment<PlaygroundViewModel, PlaygroundListItem>(R.string.playground_title) {

    override val viewModel by viewModel<PlaygroundViewModel>()

    override fun createAdapter() = PlaygroundAdapter(viewModel.viewModelScope)

    override fun getBeagleModules(): List<Module<*>> = listOf(
        ButtonModule(text = "Button", onButtonPressed = {}),
        CheckBoxModule(text = "Checkbox", onValueChanged = {}),
        ItemListModule<BeagleListItemContract>(
            title = "ItemList",
            items = listOf(),
            onItemSelected = {}
        ),
        KeyValueListModule(
            title = "KeyValueList",
            pairs = listOf(
                "Key 1" to "Value 1",
                "Key 2" to "Value 2",
                "Key 3" to "Value 3"
            )
        ),
        LabelModule(title = "Label"),
        MultipleSelectionListModule<BeagleListItemContract>(
            title = "MultipleSelectionList",
            items = listOf(),
            initiallySelectedItemIds = emptySet(),
            onSelectionChanged = {}
        ),
        SingleSelectionListModule<BeagleListItemContract>(
            title = "SingleSelectionList",
            items = listOf(),
            initiallySelectedItemId = null,
            onSelectionChanged = {}
        ),
        SwitchModule(text = "Switch", onValueChanged = {}),
        TextModule(text = "Text"),
        AnimationDurationSwitchModule(),
        AppInfoButtonModule(),
        DeviceInfoModule(),
        HeaderModule(
            title = "Header"
        )
    )

    companion object {
        fun newInstance() = PlaygroundFragment()
    }
}