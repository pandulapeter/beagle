package com.pandulapeter.beagleCore.configuration

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.IdRes
import com.pandulapeter.beagleCore.contracts.BeagleListItemContract
import com.pandulapeter.beagleCore.implementation.PendingChangeEvent
import java.util.UUID

/**
 * Contains all supported modules that can be added to the drawer.
 */
@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
sealed class Trick {

    //region Generic modules
    /**
     * Displays an empty space of specified size.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
     * @param size - The size of the space to be left empty, or null for the default padding of 8dp. Null by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class Padding(
        override val id: String = UUID.randomUUID().toString(),
        @Dimension val size: Int? = null
    ) : Trick()

    /**
     * Displays a horizontal line.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class Divider(
        override val id: String = UUID.randomUUID().toString()
    ) : Trick()

    /**
     * Displays simple text content.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
     * @param text - The text that should be displayed.
     * @param isTitle - Whether or not the text should appear in bold style. False by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class Text(
        override val id: String = UUID.randomUUID().toString(),
        val text: CharSequence,
        val isTitle: Boolean = false
    ) : Trick()

    /**
     * Displays a longer piece of text that can be collapsed into a title.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
     * @param title - The title of the module.
     * @param text - The text that should be displayed.
     * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class LongText(
        override val id: String = UUID.randomUUID().toString(),
        override val title: CharSequence,
        val text: CharSequence,
        override val isInitiallyExpanded: Boolean = false
    ) : Trick(), Expandable {

        override var isExpanded = isInitiallyExpanded
            private set

        override fun toggleExpandedState() {
            isExpanded = !isExpanded
        }
    }

    /**
     * Displays a drawable.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
     * @param drawable - The drawable that should be displayed.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class Image(
        override val id: String = UUID.randomUUID().toString(),
        val drawable: Drawable?
    ) : Trick()

    /**
     * Allows the user to adjust a numeric value..
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
     * @param name - A lambda that returns the name that should appear above the slider in function of its current value.
     * @param minimumValue - The minimum value supported by the slider. 0 by default.
     * @param maximumValue - The maximum value supported by the slider. 10 by default.
     * @param initialValue - The initial value of the slider. By default it's the same as the slider's minimum value.
     * @param needsConfirmation - If true, an "Apply" button will appear after any modification and the onValueChanged lambda will only get called after the user presses that button. False by default.
     * @param onValueChanged - Callback that gets invoked when the user changes the value of the slider.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class Slider(
        override val id: String = UUID.randomUUID().toString(),
        val name: (value: Int) -> CharSequence,
        val minimumValue: Int = 0,
        val maximumValue: Int = 10,
        override val initialValue: Int = minimumValue,
        override val needsConfirmation: Boolean = false,
        override val onValueChanged: (value: Int) -> Unit
    ) : Trick(), Confirmable<Int> {

        override var currentValue = initialValue
            set(value) {
                if (field != value) {
                    field = value
                    onCurrentValueChanged(value)
                }
            }
        override var savedValue = initialValue

    }

    /**
     * Displays a switch with configurable title and behavior - ideal for feature toggles.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
     * @param title - The text that appears near the switch. "Keyline overlay" by default.
     * @param initialValue - The initial value of the toggle. False by default.
     * @param needsConfirmation - If true, an "Apply" button will appear after any modification and the onValueChanged lambda will only get called after the user presses that button. False by default.
     * @param onValueChanged - Callback that gets invoked when the user changes the value of the toggle.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class Toggle(
        override val id: String = UUID.randomUUID().toString(),
        val title: CharSequence,
        override val initialValue: Boolean = false,
        override val needsConfirmation: Boolean = false,
        override val onValueChanged: (newValue: Boolean) -> Unit
    ) : Trick(), Confirmable<Boolean> {

        override var currentValue = initialValue
            set(value) {
                if (field != value) {
                    field = value
                    onCurrentValueChanged(value)
                }
            }
        override var savedValue = initialValue
    }

    /**
     * Displays a button with configurable text and action.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
     * @param text - The text that should be displayed on the button.
     * @param onButtonPressed - The callback that gets invoked when the user presses the button.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class Button(
        override val id: String = UUID.randomUUID().toString(),
        val text: CharSequence,
        val onButtonPressed: () -> Unit
    ) : Trick()

    /**
     * Displays a list of key-value pairs that can be collapsed into a title.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the module, a suitable default value is auto-generated.
     * @param title - The text that appears in the header of the module.
     * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
     * @param pairs - The list of key-value pairs.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class KeyValue(
        override val id: String = UUID.randomUUID().toString(),
        override val title: CharSequence,
        override val isInitiallyExpanded: Boolean = false,
        val pairs: List<Pair<CharSequence, CharSequence>>
    ) : Trick(), Expandable {

        override var isExpanded = isInitiallyExpanded
            private set

        override fun toggleExpandedState() {
            isExpanded = !isExpanded
        }
    }

    /**
     * Displays an expandable list of custom items and exposes a callback when the user makes a selection. A possible use case could be providing a list of test accounts to make the authentication flow faster.
     * The class is generic to a representation of a list item which must implement the [BeagleListItemContract] interface.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the list, a suitable default value is auto-generated.
     * @param title - The text that appears in the header of the module.
     * @param items - The hardcoded list of items implementing the [BeagleListItemContract] interface.
     * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
     * @param onItemSelected - The callback that will get executed when an item is selected.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class SimpleList<T : BeagleListItemContract>(
        override val id: String = UUID.randomUUID().toString(),
        override val title: CharSequence,
        override val isInitiallyExpanded: Boolean = false,
        val items: List<T>,
        private val onItemSelected: (item: T) -> Unit
    ) : Trick(), Expandable {

        override var isExpanded = isInitiallyExpanded
            private set

        override fun toggleExpandedState() {
            isExpanded = !isExpanded
        }

        fun invokeItemSelectedCallback(id: String) = onItemSelected(items.first { it.id == id })
    }

    /**
     * Displays a list of radio buttons. A possible use case could be changing the base URL of the application to simplify testing on different backend environments.
     * The class is generic to a representation of a list item which must implement the [BeagleListItemContract] interface.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the list, a suitable default value is auto-generated.
     * @param title - The text that appears in the header of the module.
     * @param items - The hardcoded list of items implementing the [BeagleListItemContract] interface.
     * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
     * @param initialSelectionId - The ID of the item that is selected when the drawer is opened for the first time, or null if no selection should be made initially. Null by default.
     * @param needsConfirmation - If true, an "Apply" button will appear after any modification and the onItemSelectionChanged lambda will only get called after the user presses that button. False by default.
     * @param onItemSelectionChanged - The callback that will get executed when the selected item is changed.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class SingleSelectionList<T : BeagleListItemContract>(
        override val id: String = UUID.randomUUID().toString(),
        override val title: CharSequence,
        override val isInitiallyExpanded: Boolean = false,
        val items: List<T>,
        private val initialSelectionId: String? = null,
        override val needsConfirmation: Boolean = false,
        private val onItemSelectionChanged: (selectedItem: T) -> Unit
    ) : Trick(), Expandable, Confirmable<String?> {

        override val initialValue: String? = initialSelectionId
        override val onValueChanged: (String?) -> Unit = { id -> onItemSelectionChanged(items.first { it.id == id }) }
        override var isExpanded = isInitiallyExpanded
            private set
        override var currentValue = initialSelectionId
            set(value) {
                if (field != value) {
                    field = value
                    onCurrentValueChanged(value)
                }
            }
        override var savedValue = initialSelectionId

        override fun toggleExpandedState() {
            isExpanded = !isExpanded
        }

        fun invokeItemSelectedCallback(id: String) {
            currentValue = id
        }
    }

    /**
     * Displays a list of checkboxes.
     * The class is generic to a representation of a list item which must implement the [BeagleListItemContract] interface.
     * This module can be added multiple times as long as the ID is unique.
     *
     * @param id - A unique ID for the module. If you don't intend to dynamically remove / modify the list, a suitable default value is auto-generated.
     * @param title - The text that appears in the header of the module.
     * @param items - The hardcoded list of items implementing the [BeagleListItemContract] interface.
     * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
     * @param initialSelectionIds - The ID-s of the items that are selected when the drawer is opened for the first time. Empty list by default.
     * @param needsConfirmation - If true, an "Apply" button will appear after any modification and the onItemSelectionChanged lambda will only get called after the user presses that button. False by default.
     * @param onItemSelectionChanged - The callback that will get executed when the list of selected items is changed.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class MultipleSelectionList<T : BeagleListItemContract>(
        override val id: String = UUID.randomUUID().toString(),
        override val title: CharSequence,
        override val isInitiallyExpanded: Boolean = false,
        val items: List<T>,
        private val initialSelectionIds: List<String> = emptyList(),
        override val needsConfirmation: Boolean = false,
        private val onItemSelectionChanged: (selectedItems: List<T>) -> Unit
    ) : Trick(), Expandable, Confirmable<List<String>> {

        override val initialValue = initialSelectionIds
        override val onValueChanged: (List<String>) -> Unit = { ids -> onItemSelectionChanged(items.filter { ids.contains(it.id) }) }
        override var isExpanded = isInitiallyExpanded
            private set
        override var currentValue = initialValue.sorted()
            set(value) {
                if (field != value) {
                    field = value
                    onCurrentValueChanged(value)
                }
            }
        override var savedValue = initialValue.sorted()

        override fun toggleExpandedState() {
            isExpanded = !isExpanded
        }

        fun invokeItemSelectedCallback(id: String) {
            currentValue = currentValue.toMutableList().apply {
                if (contains(id)) {
                    remove(id)
                } else {
                    add(id)
                }
            }.sorted()
        }
    }

    /**
     * Displays an expandable list of your custom logs. An example use case could be logging analytics events.
     * Use Beagle.log() to push a new message to the top of the list.
     * This module can be added multiple times as long as the tag is unique.
     *
     * @param title - The title of the module. "Logs" by default.
     * @param tag - The tag for which the logs should be filtered, or null for no filtering. Null by default.
     * @param maxItemCount - The maximum number of messages that will appear when expanded. 10 by default.
     * @param shouldShowTimestamp - Whether or not each message should display the timestamp when it was added. False by default.
     * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class LogList(
        override val title: CharSequence = "Logs",
        val tag: String? = null,
        val maxItemCount: Int = 10,
        val shouldShowTimestamp: Boolean = false,
        override val isInitiallyExpanded: Boolean = false
    ) : Trick(), Expandable {

        override val id = "logList_$tag"
        override var isExpanded = isInitiallyExpanded
            private set

        override fun toggleExpandedState() {
            isExpanded = !isExpanded
        }
    }
    //endregion

    //region Unique modules
    /**
     * Displays a header on top of the drawer with general information about the app / build.
     * This module can only be added once and will always appear on top.
     *
     * @param title - The title of the app / the debug menu. Null by default (hidden title).
     * @param subtitle - The subtitle of the the debug menu. Consider using the build version ("v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"). Null by default (hidden subtitle).
     * @param text - Additional text you want to display on the debug drawer. Null by default (hidden text).
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class Header(
        val title: CharSequence? = null,
        val subtitle: CharSequence? = null,
        val text: CharSequence? = null
    ) : Trick() {

        override val id = ID

        companion object {
            const val ID = "header"
        }
    }

    /**
     * Displays a switch that, when enabled, draws a grid over your app with configurable dimensions that you can use to check the alignments of your Views.
     * This module can only be added once.
     *
     * @param title - The text that appears near the switch. "Keyline overlay" by default.
     * @param keylineGrid - The distance between the grid lines. 8dp by default.
     * @param keylinePrimary - The distance between the edge of the screen and the primary keyline. 16dp by default (24dp on tablets).
     * @param keylinePrimary - The distance between the edge of the screen and the secondary keyline. 72dp by default (80dp on tablets).
     * @param gridColor - The color to be used when drawing the grid. By default it will be the debug menu's text color.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class KeylineOverlayToggle(
        val title: CharSequence = "Keyline overlay",
        @Dimension val keylineGrid: Int? = null,
        @Dimension val keylinePrimary: Int? = null,
        @Dimension val keylineSecondary: Int? = null,
        @ColorInt val gridColor: Int? = null
    ) : Trick() {

        override val id = ID

        companion object {
            const val ID = "keylineOverlayToggle"
        }
    }

    /**
     * Displays a switch that, when enabled, draws rectangles matching the bounds for every View in your hierarchy so that you can verify sizes and paddings.
     * This module can only be added once.
     *
     * @param title - The text that appears near the switch. "Show view bounds" by default.
     * @param color - The color to be used when drawing the overlay. By default it will be the debug menu's text color.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class ViewBoundsOverlayToggle(
        val title: CharSequence = "Show view bounds",
        @ColorInt val color: Int? = null
    ) : Trick() {

        override val id = ID

        companion object {
            const val ID = "viewBoundsOverlayToggle"
        }
    }

    /**
     * Displays a switch that, when enabled, increases the duration of animations.
     * This module can only be added once.
     *
     * @param title - The text that appears near the switch. "Slow down animations" by default.
     * @param multiplier - The multiplier that should be applied for all animation durations. 4f by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class AnimatorDurationToggle(
        val title: CharSequence = "Slow down animations",
        val multiplier: Float = 4f
    ) : Trick() {

        override val id = ID

        companion object {
            const val ID = "animatorDurationToggle"
        }
    }

    /**
     * Displays a button that links to the Android App Info page for your app.
     * This module can only be added once.
     *
     * @param text - The text that should be displayed on the button. "Show app info" by default.
     * @param shouldOpenInNewTask - Whether or not the App Info page will be opened with the Intent.FLAG_ACTIVITY_NEW_TASK flag. False by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class AppInfoButton(
        val text: CharSequence = "Show app info",
        val shouldOpenInNewTask: Boolean = false
    ) : Trick() {

        override val id = ID

        companion object {
            const val ID = "appInfoButton"
        }
    }

    /**
     * Displays a button that takes a screenshot of the current layout and allows the user to share it.
     * This module can only be added once.
     *
     * @param text - The text that should be displayed on the button. "Take a screenshot" by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class ScreenshotButton(
        val text: CharSequence = "Take a screenshot"
    ) : Trick() {

        override val id = ID

        companion object {
            const val ID = "screenshotButton"
        }
    }

    /**
     * Displays a button that throws an exception when pressed - useful for testing crash reporting.
     * This module can only be added once.
     *
     * @param text - The text that should be displayed on the button. "Force crash" by default.
     * @param message - The detail message of the exception. "Test crash: Beagle was a bad boy." by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class ForceCrashButton(
        val text: CharSequence = "Force crash",
        val message: String = "Test crash: Beagle was a bad boy."
    ) : Trick() {

        override val id = ID

        companion object {
            const val ID = "forceCrashButton"
        }
    }

    /**
     * Displays a button that generates a random string and sets it as the text for a specified EditText widget, copies it to the clipboard or exposes it through a custom callback.
     * This module can only be added once.
     *
     * @param text - The text that should be displayed on the button. "Generate Lorem Ipsum" by default.
     * @param editTextId - The ID of the EditText widget where the generated text should go. If both this and onStringReady are null, the text will be copied to the clipboard. Null by default.
     * @param minimumWordCount - The minimum number of words to be generated (must be larger then 1 and smaller than maximumWordCount). 10 by default.
     * @param maximumWordCount - The maximum number of words to be generated (must be larger or equal to minimumWordCount). 40 by default.
     * @param onStringReady - A callback that can be implemented for custom handling of the generated string. It will only be called if editTextId is null. If both this and editTextId are null, the text will be copied to the clipboard. Null by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class LoremIpsumButton(
        val text: CharSequence = "Generate Lorem Ipsum",
        @IdRes val editTextId: Int? = null,
        val minimumWordCount: Int = 10,
        val maximumWordCount: Int = 40,
        val onStringReady: ((loremIpsum: String) -> Unit)? = null
    ) : Trick() {

        override val id = ID

        companion object {
            const val ID = "loremIpsumButton"
        }
    }

    /**
     * Displays an expandable list of historical network activity.
     * Configure BeagleNetworkInterceptor to your OkHttpClient to start tracking requests.
     * This module can only be added once.
     *
     * @param title - The title of the module. "Network activity" by default.
     * @param baseUrl - When not empty, all URL-s will have the specified String filtered out. Empty by default.
     * @param shouldShowHeaders - Whether of not the detail dialog should also contain the request / response headers. False by default.
     * @param maxItemCount - The maximum number of messages that will appear when expanded. 10 by default.
     * @param shouldShowTimestamp - Whether or not each message should display the timestamp when it was added. False by default.
     * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class NetworkLogList(
        override val title: CharSequence = "Network activity",
        val baseUrl: String = "",
        val shouldShowHeaders: Boolean = false,
        val maxItemCount: Int = 10,
        val shouldShowTimestamp: Boolean = false,
        override val isInitiallyExpanded: Boolean = false
    ) : Trick(), Expandable {

        override val id = ID
        override var isExpanded = isInitiallyExpanded
            private set

        override fun toggleExpandedState() {
            isExpanded = !isExpanded
        }

        companion object {
            const val ID = "networkLogging"
        }
    }

    /**
     * Displays information about the current device and the OS.
     * This module can only be added once.
     *
     * @param title - The title of the module. "Device information" by default.
     * @param isInitiallyExpanded - Whether or not the list should be expanded when the drawer is opened for the first time. False by default.
     */
    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    data class DeviceInformationKeyValue(
        override val title: CharSequence = "Device information",
        override val isInitiallyExpanded: Boolean = false
    ) : Trick(), Expandable {

        override val id = ID
        override var isExpanded = isInitiallyExpanded
            private set

        override fun toggleExpandedState() {
            isExpanded = !isExpanded
        }

        companion object {
            const val ID = "deviceInformation"
        }
    }
    //endregion

    //region Implementation details
    abstract val id: String

    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    interface Expandable {

        val id: String
        val title: CharSequence
        val isInitiallyExpanded: Boolean
        val isExpanded: Boolean

        fun toggleExpandedState()
    }

    @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
    interface Confirmable<T> {
        val id: String
        val needsConfirmation: Boolean
        val initialValue: T
        var currentValue: T
        var savedValue: T
        val onValueChanged: (T) -> Unit

        fun updateSavedValueAndNotifyUser(value: T) {
            onValueChanged(value)
            savedValue = value
        }

        fun onCurrentValueChanged(newValue: T) {
            if (needsConfirmation) {
                if (currentValue == savedValue) {
                    removePendingChange(id)
                } else {
                    addPendingChange(PendingChangeEvent(
                        trickId = id,
                        apply = { updateSavedValueAndNotifyUser(newValue) },
                        reset = { currentValue = savedValue }
                    ))
                }
            } else {
                updateSavedValueAndNotifyUser(newValue)
            }
        }
    }

    //TODO: Should be encapsulated + should not be part of the noop variant. Needs refactoring.
    companion object {
        var pendingChangeListener: (() -> Unit)? = null
        var onAllChangesApplied: (() -> Unit)? = null
        private var pendingChanges = emptyList<PendingChangeEvent>()
        val hasPendingChanges get() = pendingChanges.isNotEmpty()

        @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
        fun addPendingChange(pendingChange: PendingChangeEvent) {
            pendingChanges = (listOf(pendingChange) + pendingChanges).distinctBy { it.trickId }
            pendingChangeListener?.invoke()
        }

        @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
        fun removePendingChange(trickId: String) {
            pendingChanges = pendingChanges.filterNot { it.trickId == trickId }
            pendingChangeListener?.invoke()
        }

        @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
        fun applyPendingChanges() {
            if (pendingChanges.isNotEmpty()) {
                pendingChanges.asReversed().toList().forEach { changeEvent ->
                    pendingChanges = pendingChanges.filterNot { it.trickId == changeEvent.trickId }
                    changeEvent.apply()
                }
                pendingChangeListener?.invoke()
                onAllChangesApplied?.invoke()
            }
        }

        @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
        fun resetPendingChanges() {
            pendingChanges.forEach { changeEvent -> changeEvent.reset() }
        }

        @Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
        fun clearPendingChanges() {
            pendingChanges = emptyList()
            pendingChangeListener?.invoke()
        }
    }
    //endregion
}