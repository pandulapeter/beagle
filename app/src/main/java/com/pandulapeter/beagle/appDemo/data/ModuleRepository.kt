package com.pandulapeter.beagle.appDemo.data

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
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
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.LongTextModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.PaddingModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule

class ModuleRepository {

    private val listeners = mutableListOf<Listener>()
    private val _modules = mutableListOf(
        ModuleWrapper(
            titleResourceId = R.string.add_module_button,
            module = ButtonModule(text = "Button", onButtonPressed = {})
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_check_box,
            module = CheckBoxModule(text = "CheckBox", onValueChanged = {})
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_divider,
            module = DividerModule()
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_item_list,
            module = ItemListModule(
                title = "ItemList",
                items = listOf(
                    ListItem("Item 1"),
                    ListItem("Item 2"),
                    ListItem("Item 3")
                ),
                onItemSelected = {}
            )
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_key_value_list,
            module = KeyValueListModule(
                title = "KeyValueList",
                pairs = listOf(
                    "Key 1" to "Value 1",
                    "Key 2" to "Value 2",
                    "Key 3" to "Value 3"
                )
            )
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_label,
            module = LabelModule(title = "Label")
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_long_text,
            module = LongTextModule(
                title = "LongText",
                text = "This is a longer piece of text that only becomes visible when the user expands the header."
            )
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_multiple_selection_list,
            module = MultipleSelectionListModule(
                title = "MultipleSelectionList",
                items = listOf(
                    ListItem("Checkbox 1"),
                    ListItem("Checkbox 2"),
                    ListItem("Checkbox 3")
                ),
                initiallySelectedItemIds = emptySet(),
                onSelectionChanged = {}
            )
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_padding,
            module = PaddingModule()
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_single_selection_list,
            module = SingleSelectionListModule(
                title = "SingleSelectionList",
                items = listOf(
                    ListItem("Radio button 1"),
                    ListItem("Radio button 2"),
                    ListItem("Radio button 3")
                ),
                initiallySelectedItemId = null,
                onSelectionChanged = {}
            )
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_switch,
            module = SwitchModule(text = "Switch", onValueChanged = {})
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_text,
            module = TextModule(text = "Text")
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_animation_duration_switch,
            module = AnimationDurationSwitchModule()
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_app_info,
            module = AppInfoButtonModule()
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_device_info,
            module = DeviceInfoModule()
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_force_crash_button,
            module = ForceCrashButtonModule()
        ),
        ModuleWrapper(
            titleResourceId = R.string.add_module_header,
            module = HeaderModule(
                title = "Header title",
                subtitle = "Header subtitle"
            )
        )
    )
    val modules: List<ModuleWrapper> get() = _modules

    @Suppress("unused")
    fun registerListener(lifecycleOwner: LifecycleOwner, listener: Listener) {
        lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
            fun onCreate() {
                if (!listeners.contains(listener)) {
                    listeners.add(listener)
                    listener.onModuleListChanged()
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                listeners.remove(listener)
            }
        })
    }

    fun addModule(moduleWrapper: ModuleWrapper) {
        _modules.add(moduleWrapper)
        notifyListeners()
    }

    private fun notifyListeners() = listeners.forEach { it.onModuleListChanged() }

    interface Listener {
        fun onModuleListChanged()
    }

    private data class ListItem(
        override val title: String
    ) : BeagleListItemContract {

        override val id = title
    }
}