package com.pandulapeter.beagle.appDemo.data

import androidx.annotation.StringRes
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.DeviceInfoModule
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.ForceCrashButtonModule
import com.pandulapeter.beagle.modules.HeaderModule
import com.pandulapeter.beagle.modules.ItemListModule
import com.pandulapeter.beagle.modules.KeyValueListModule
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.LogListModule
import com.pandulapeter.beagle.modules.LongTextModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule
import java.util.UUID

sealed class ModuleWrapper(
    @StringRes val titleResourceId: Int,
    @StringRes val descriptionResourceId: Int,
    val module: Module<*>,
    val codeSnippet: String
) {
    val id get() = module.id

    class Button : ModuleWrapper(
        titleResourceId = R.string.add_module_button,
        descriptionResourceId = R.string.add_module_button_description,
        module = ButtonModule(text = "Button", onButtonPressed = {}),
        codeSnippet = "ButtonModule(text = \"Button\", onButtonPressed = {})"
    )

    class CheckBox : ModuleWrapper(
        titleResourceId = R.string.add_module_check_box,
        descriptionResourceId = R.string.add_module_check_box_description,
        module = CheckBoxModule(text = "CheckBox", initialValue = false, onValueChanged = {}),
        codeSnippet = "CheckBoxModule(text = \"CheckBox\", initialValue = false, onValueChanged = {})"
    )

    class Divider : ModuleWrapper(
        titleResourceId = R.string.add_module_divider,
        descriptionResourceId = R.string.add_module_divider_description,
        module = DividerModule(),
        codeSnippet = "DividerModule()"
    )

    class ItemList : ModuleWrapper(
        titleResourceId = R.string.add_module_item_list,
        descriptionResourceId = R.string.add_module_item_list_description,
        module = ItemListModule(
            title = "ItemList",
            items = listOf(
                BeagleListItemContractImplementation("Item 1"),
                BeagleListItemContractImplementation("Item 2"),
                BeagleListItemContractImplementation("Item 3")
            ),
            onItemSelected = {}
        ),
        codeSnippet = "ItemListModule(\n" +
                "    title = \"ItemList\",\n" +
                "    items = listOf(\n" +
                "        BeagleListItemContractImplementation(\"Item 1\"),\n" +
                "        BeagleListItemContractImplementation(\"Item 2\"),\n" +
                "        BeagleListItemContractImplementation(\"Item 3\")\n" +
                "    ),\n" +
                "    onItemSelected = {}\n" +
                ")"
    )

    class KeyValueList : ModuleWrapper(
        titleResourceId = R.string.add_module_key_value_list,
        descriptionResourceId = R.string.add_module_key_value_list_description,
        module = KeyValueListModule(
            title = "KeyValueList",
            pairs = listOf(
                "Key 1" to "Value 1",
                "Key 2" to "Value 2",
                "Key 3" to "Value 3"
            )
        ),
        codeSnippet = "KeyValueListModule(\n" +
                "    title = \"KeyValueList\",\n" +
                "    pairs = listOf(\n" +
                "        \"Key 1\" to \"Value 1\",\n" +
                "        \"Key 2\" to \"Value 2\",\n" +
                "        \"Key 3\" to \"Value 3\"\n" +
                "    )\n" +
                ")"
    )

    class Label : ModuleWrapper(
        titleResourceId = R.string.add_module_label,
        descriptionResourceId = R.string.add_module_label_description,
        module = LabelModule(title = "Label"),
        codeSnippet = "LabelModule(title = \"Label\")"
    )

    class LogList : ModuleWrapper(
        titleResourceId = R.string.add_module_log_list,
        descriptionResourceId = R.string.add_module_log_list_description,
        module = UUID.randomUUID().toString().let { tag ->
            LogListModule(
                tag = tag
            ).also {
                Beagle.log(message = "Log message 3", tag = tag)
                Beagle.log(message = "Log message 2", tag = tag)
                Beagle.log(message = "Log message 1", tag = tag)
            }
        },
        codeSnippet = "LogListModule(tag = …)"
    )

    class LongText : ModuleWrapper(
        titleResourceId = R.string.add_module_long_text,
        descriptionResourceId = R.string.add_module_long_text_description,
        module = LongTextModule(
            title = "LongText",
            text = "This is a longer piece of text that only becomes visible when the user expands the header."
        ),
        codeSnippet = "LongTextModule(\n" +
                "    title = \"LongText\",\n" +
                "    text = \"This is a longer piece of text that only becomes visible when the user expands the header.\"\n" +
                ")"
    )

    class MultipleSelectionList : ModuleWrapper(
        titleResourceId = R.string.add_module_multiple_selection_list,
        descriptionResourceId = R.string.add_module_multiple_selection_list_description,
        module =
        MultipleSelectionListModule(
            title = "MultipleSelectionList",
            items = listOf(
                BeagleListItemContractImplementation("Checkbox 1"),
                BeagleListItemContractImplementation("Checkbox 2"),
                BeagleListItemContractImplementation("Checkbox 3")
            ),
            initiallySelectedItemIds = emptySet(),
            onSelectionChanged = {}
        ),
        codeSnippet = "MultipleSelectionListModule(\n" +
                "    title = \"MultipleSelectionList\",\n" +
                "    items = listOf(\n" +
                "        BeagleListItemContractImplementation(\"Checkbox 1\"),\n" +
                "        BeagleListItemContractImplementation(\"Checkbox 2\"),\n" +
                "        BeagleListItemContractImplementation(\"Checkbox 3\")\n" +
                "    ),\n" +
                "    initiallySelectedItemIds = emptySet(),\n" +
                "    onSelectionChanged = {}\n" +
                ")"
    )

    class Padding : ModuleWrapper(
        titleResourceId = R.string.add_module_padding,
        descriptionResourceId = R.string.add_module_padding_description,
        module = PaddingModule(),
        codeSnippet = "PaddingModule()"
    )

    class SingleSelectionList : ModuleWrapper(
        titleResourceId = R.string.add_module_single_selection_list,
        descriptionResourceId = R.string.add_module_single_selection_list_description,
        module =
        SingleSelectionListModule(
            title = "SingleSelectionList",
            items = listOf(
                BeagleListItemContractImplementation("Radio button 1"),
                BeagleListItemContractImplementation("Radio button 2"),
                BeagleListItemContractImplementation("Radio button 3")
            ),
            initiallySelectedItemId = null,
            onSelectionChanged = {}
        ),
        codeSnippet = "SingleSelectionListModule(\n" +
                "    title = \"SingleSelectionList\",\n" +
                "    items = listOf(\n" +
                "        BeagleListItemContractImplementation(\"Radio button 1\"),\n" +
                "        BeagleListItemContractImplementation(\"Radio button 2\"),\n" +
                "        BeagleListItemContractImplementation(\"Radio button 3\")\n" +
                "    ),\n" +
                "    initiallySelectedItemId = null,\n" +
                "    onSelectionChanged = {}\n" +
                ")"
    )

    class Switch : ModuleWrapper(
        titleResourceId = R.string.add_module_switch,
        descriptionResourceId = R.string.add_module_switch_description,
        module = SwitchModule(text = "Switch", onValueChanged = {}),
        codeSnippet = "SwitchModule(text = \"Switch\", onValueChanged = {})"
    )

    class Text : ModuleWrapper(
        titleResourceId = R.string.add_module_text,
        descriptionResourceId = R.string.add_module_text_description,
        module = TextModule(text = "Text"),
        codeSnippet = "TextModule(text = \"Text\")"
    )

    object AnimationDurationSwitch : ModuleWrapper(
        titleResourceId = R.string.add_module_animation_duration_switch,
        descriptionResourceId = R.string.add_module_animation_duration_switch_description,
        module = AnimationDurationSwitchModule(),
        codeSnippet = "AnimationDurationSwitchModule()"
    )

    object AppInfoButton : ModuleWrapper(
        titleResourceId = R.string.add_module_app_info,
        descriptionResourceId = R.string.add_module_app_info_description,
        module = AppInfoButtonModule(),
        codeSnippet = "AppInfoButtonModule()"
    )

    object DeviceInfo : ModuleWrapper(
        titleResourceId = R.string.add_module_device_info,
        descriptionResourceId = R.string.add_module_device_info_description,
        module = DeviceInfoModule(),
        codeSnippet = "DeviceInfoModule()"
    )

    object ForceCrashButton : ModuleWrapper(
        titleResourceId = R.string.add_module_force_crash_button,
        descriptionResourceId = R.string.add_module_force_crash_button_description,
        module = ForceCrashButtonModule(),
        codeSnippet = "ForceCrashButtonModule()"
    )

    object Header : ModuleWrapper(
        titleResourceId = R.string.add_module_header,
        descriptionResourceId = R.string.add_module_header_description,
        module = HeaderModule(
            title = "Header title",
            subtitle = "Header subtitle"
        ),
        codeSnippet = "HeaderModule(\n" +
                "    title = \"Header title\",\n" +
                "    subtitle = \"Header subtitle\"\n" +
                ")"
    )

    object KeylineOverlaySwitch : ModuleWrapper(
        titleResourceId = R.string.add_module_keyline_overlay_switch,
        descriptionResourceId = R.string.add_module_keyline_overlay_switch_description,
        module = KeylineOverlaySwitchModule(),
        codeSnippet = "KeylineOverlaySwitchModule()"
    )
}