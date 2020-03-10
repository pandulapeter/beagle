package com.pandulapeter.beagle.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.models.LogItem
import com.pandulapeter.beagle.models.NetworkLogItem
import com.pandulapeter.beagle.views.BeagleDrawerLayout
import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel
import com.pandulapeter.beagle.views.drawerItems.button.ButtonViewModel
import com.pandulapeter.beagle.views.drawerItems.divider.DividerViewModel
import com.pandulapeter.beagle.views.drawerItems.header.HeaderViewModel
import com.pandulapeter.beagle.views.drawerItems.image.ImageViewModel
import com.pandulapeter.beagle.views.drawerItems.keyValue.KeyValueItemViewModel
import com.pandulapeter.beagle.views.drawerItems.listHeader.ListHeaderViewModel
import com.pandulapeter.beagle.views.drawerItems.listItem.ListItemViewModel
import com.pandulapeter.beagle.views.drawerItems.logItem.LogItemViewModel
import com.pandulapeter.beagle.views.drawerItems.longText.LongTextViewModel
import com.pandulapeter.beagle.views.drawerItems.multipleSelectionListItem.MultipleSelectionListItemViewModel
import com.pandulapeter.beagle.views.drawerItems.networkLogItem.NetworkLogItemViewModel
import com.pandulapeter.beagle.views.drawerItems.padding.PaddingViewModel
import com.pandulapeter.beagle.views.drawerItems.singleSelectionListItem.SingleSelectionListItemViewModel
import com.pandulapeter.beagle.views.drawerItems.slider.SliderViewModel
import com.pandulapeter.beagle.views.drawerItems.text.TextViewModel
import com.pandulapeter.beagle.views.drawerItems.toggle.ToggleViewModel
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.Trick

internal fun Context.animatedDrawable(@DrawableRes drawableId: Int) = AnimatedVectorDrawableCompat.create(this, drawableId)

@SuppressLint("ResourceAsColor")
@ColorInt
internal fun Context.colorResource(@AttrRes id: Int): Int {
    val resolvedAttr = TypedValue()
    theme.resolveAttribute(id, resolvedAttr, true)
    return ContextCompat.getColor(this, resolvedAttr.run { if (resourceId != 0) resourceId else data })
}

internal fun Context.dimension(@DimenRes dimensionResInt: Int) = resources.getDimensionPixelSize(dimensionResInt)

internal fun Context.drawable(@DrawableRes drawableResId: Int) = AppCompatResources.getDrawable(this, drawableResId)

internal fun View.hideKeyboard() {
    clearFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
}

internal inline fun <T : Fragment> T.withArguments(bundleOperations: (Bundle) -> Unit): T = apply {
    arguments = Bundle().apply { bundleOperations(this) }
}

internal fun View.setBackgroundFromWindowBackground() {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
    if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
        setBackgroundColor(typedValue.data)
    } else {
        background = context.drawable(typedValue.resourceId)
    }
}

internal var View.visibleOrInvisible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.INVISIBLE
    }

internal var View.visibleOrGone
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

internal fun Activity.findRootViewGroup(): ViewGroup = findViewById(android.R.id.content) ?: window.decorView.findViewById(android.R.id.content)

private fun MutableList<DrawerItemViewModel>.addListModule(trick: Trick.Expandable, shouldShowIcon: Boolean, addItems: () -> List<DrawerItemViewModel>) {
    add(
        ListHeaderViewModel(
            id = trick.id,
            title = trick.title,
            isExpanded = trick.isExpanded,
            shouldShowIcon = shouldShowIcon,
            onItemSelected = {
                trick.toggleExpandedState()
                Beagle.updateItems()
            }
        )
    )
    if (trick.isExpanded) {
        addAll(addItems().distinctBy { it.id })
    }
}

internal fun List<Trick>.mapToViewModels(appearance: Appearance, networkLogItems: List<NetworkLogItem>, logItems: List<LogItem>): List<DrawerItemViewModel> {
    val items = mutableListOf<DrawerItemViewModel>()
    forEach { trick ->
        when (trick) {
            is Trick.Divider -> items.add(
                DividerViewModel(
                    id = trick.id
                )
            )
            is Trick.Padding -> items.add(
                PaddingViewModel(
                    id = trick.id,
                    size = trick.size
                )
            )
            is Trick.Text -> items.add(
                TextViewModel(
                    id = trick.id,
                    text = trick.text,
                    isTitle = trick.isTitle
                )
            )
            is Trick.LongText -> items.addListModule(
                trick = trick,
                shouldShowIcon = true,
                addItems = {
                    listOf(
                        LongTextViewModel(
                            id = "longText_${trick.id}",
                            text = trick.text
                        )
                    )
                }
            )
            is Trick.Image -> items.add(
                ImageViewModel(
                    id = trick.id,
                    drawable = trick.drawable
                )
            )
            is Trick.Slider -> items.add(
                SliderViewModel(trick = trick)
            )
            is Trick.Toggle -> items.add(
                ToggleViewModel(
                    id = trick.id,
                    title = trick.title,
                    isEnabled = trick.value,
                    onToggleStateChanged = { newValue ->
                        trick.value = newValue
                        Beagle.updateItems()
                    }
                )
            )
            is Trick.Button -> items.add(
                ButtonViewModel(
                    id = trick.id,
                    shouldUseListItem = appearance.shouldUseItemsInsteadOfButtons,
                    text = trick.text,
                    onButtonPressed = trick.onButtonPressed
                )
            )
            is Trick.KeyValue -> items.addListModule(
                trick = trick,
                shouldShowIcon = trick.pairs.isNotEmpty(),
                addItems = {
                    trick.pairs.map { pair ->
                        KeyValueItemViewModel(
                            trickId = trick.id,
                            pair = pair
                        )
                    }
                }
            )
            is Trick.SimpleList<*> -> items.addListModule(
                trick = trick,
                shouldShowIcon = trick.items.isNotEmpty(),
                addItems = {
                    trick.items.map { item ->
                        ListItemViewModel(
                            listModuleId = trick.id,
                            item = item,
                            onItemSelected = { trick.invokeItemSelectedCallback(item.id) }
                        )
                    }
                }
            )
            is Trick.SingleSelectionList<*> -> items.addListModule(
                trick = trick,
                shouldShowIcon = trick.items.isNotEmpty(),
                addItems = {
                    trick.items.map { item ->
                        SingleSelectionListItemViewModel(
                            listModuleId = trick.id,
                            item = item,
                            isSelected = trick.selectedItemId == item.id,
                            onItemSelected = { itemId ->
                                trick.invokeItemSelectedCallback(itemId)
                                Beagle.updateItems()
                            }
                        )
                    }
                }
            )
            is Trick.MultipleSelectionList<*> -> items.addListModule(
                trick = trick,
                shouldShowIcon = trick.items.isNotEmpty(),
                addItems = {
                    trick.items.map { item ->
                        MultipleSelectionListItemViewModel(
                            listModuleId = trick.id,
                            item = item,
                            isSelected = trick.selectedItemIds.contains(item.id),
                            onItemSelected = { itemId -> trick.invokeItemSelectedCallback(itemId) }
                        )
                    }
                }
            )
            is Trick.LogList -> (if (trick.tag == null) logItems else logItems.filter { it.tag == trick.tag }).take(trick.maxItemCount).let { logItems ->
                items.addListModule(
                    trick = trick,
                    shouldShowIcon = logItems.isNotEmpty(),
                    addItems = {
                        logItems.map { logItem ->
                            LogItemViewModel(
                                logListTrick = trick,
                                logItem = logItem,
                                onItemSelected = { Beagle.openLogPayloadDialog(logItem) }
                            )
                        }
                    }
                )
            }
            is Trick.Header -> items.add(
                HeaderViewModel(
                    headerTrick = trick
                )
            )
            is Trick.KeylineOverlayToggle -> items.add(
                ToggleViewModel(
                    id = trick.id,
                    title = trick.title,
                    isEnabled = Beagle.isKeylineOverlayEnabled,
                    onToggleStateChanged = { newValue ->
                        Beagle.isKeylineOverlayEnabled = newValue
                    })
            )
            is Trick.ViewBoundsOverlayToggle -> items.add(
                ToggleViewModel(
                    id = trick.id,
                    title = trick.title,
                    isEnabled = Beagle.isViewBoundsOverlayEnabled,
                    onToggleStateChanged = { newValue ->
                        Beagle.isViewBoundsOverlayEnabled = newValue
                    })
            )
            is Trick.AnimatorDurationToggle -> items.add(
                ToggleViewModel(
                    id = trick.id,
                    title = trick.title,
                    isEnabled = Beagle.animationDurationMultiplier != 1f,
                    onToggleStateChanged = { newValue ->
                        Beagle.animationDurationMultiplier = if (newValue) trick.multiplier else 1f
                    })
            )
            is Trick.AppInfoButton -> items.add(
                ButtonViewModel(
                    id = trick.id,
                    shouldUseListItem = appearance.shouldUseItemsInsteadOfButtons,
                    text = trick.text,
                    onButtonPressed = {
                        Beagle.dismiss()
                        Beagle.currentActivity?.run {
                            startActivity(Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", packageName, null)
                            })
                        }
                    })
            )
            is Trick.ScreenshotButton -> items.add(
                ButtonViewModel(
                    id = trick.id,
                    shouldUseListItem = appearance.shouldUseItemsInsteadOfButtons,
                    text = trick.text,
                    onButtonPressed = {
                        (Beagle.drawers[Beagle.currentActivity]?.parent as? BeagleDrawerLayout?)?.takeAndShareScreenshot()
                        Beagle.dismiss()
                    }
                )
            )
            is Trick.ForceCrashButton -> items.add(
                ButtonViewModel(
                    id = trick.id,
                    shouldUseListItem = appearance.shouldUseItemsInsteadOfButtons,
                    text = trick.text,
                    onButtonPressed = { throw RuntimeException(trick.message) }
                )
            )
            is Trick.LoremIpsumButton -> items.add(
                ButtonViewModel(
                    id = trick.id,
                    shouldUseListItem = appearance.shouldUseItemsInsteadOfButtons,
                    text = trick.text,
                    onButtonPressed = {
                        val generatedText = LoremIpsumGenerator.generateSentence(trick.minimumWordCount, trick.maximumWordCount)
                        trick.editTextId.also { editTextId ->
                            if (editTextId == null) {
                                trick.onStringReady.let { onStringReady ->
                                    if (onStringReady == null) {
                                        (Beagle.currentActivity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)?.setPrimaryClip(
                                            ClipData.newPlainText(generatedText, generatedText)
                                        )
                                        Toast.makeText(Beagle.currentActivity, "Text copied to clipboard.", Toast.LENGTH_SHORT).show()
                                    } else {
                                        onStringReady(generatedText)
                                    }
                                }
                            } else {
                                Beagle.currentActivity?.findViewById<EditText>(editTextId)?.setText(generatedText)
                            }
                        }
                        Beagle.dismiss()
                    }
                )
            )
            is Trick.NetworkLogList -> networkLogItems.take(trick.maxItemCount).let { networkLogItems ->
                items.addListModule(
                    trick = trick,
                    shouldShowIcon = networkLogItems.isNotEmpty(),
                    addItems = {
                        networkLogItems.map { networkLogItem ->
                            NetworkLogItemViewModel(
                                networkLogListTrick = trick,
                                networkLogItem = networkLogItem,
                                onItemSelected = { Beagle.openNetworkEventBodyDialog(networkLogItem, trick.shouldShowHeaders) }
                            )
                        }
                    }
                )
            }
            is Trick.DeviceInformationKeyValue -> items.addListModule(
                trick = trick,
                shouldShowIcon = true,
                addItems = {
                    val dm = DisplayMetrics()
                    Beagle.currentActivity?.windowManager?.defaultDisplay?.getMetrics(dm)
                    listOf(
                        KeyValueItemViewModel(
                            trickId = trick.id,
                            pair = "Manufacturer" to Build.MANUFACTURER
                        ),
                        KeyValueItemViewModel(
                            trickId = trick.id,
                            pair = "Model" to Build.MODEL
                        ),
                        KeyValueItemViewModel(
                            trickId = trick.id,
                            pair = "Screen" to "${dm.widthPixels} * ${dm.heightPixels} (${dm.densityDpi} dpi)"
                        ),
                        KeyValueItemViewModel(
                            trickId = trick.id,
                            pair = "Android SDK version" to "${Build.VERSION.SDK_INT}"
                        )
                    )
                }
            )
        }
    }
    return items
}