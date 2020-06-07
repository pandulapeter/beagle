package com.pandulapeter.beagle.appExample

import android.app.Application
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.Toast
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.contracts.BeagleListItemContract
import com.pandulapeter.beagle.common.listeners.OverlayListener
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.AppInfoButtonModule
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckBoxModule
import com.pandulapeter.beagle.modules.ItemListModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.MultipleSelectionListModule
import com.pandulapeter.beagle.modules.SingleSelectionListModule
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.modules.TextModule

@Suppress("unused")
class BeagleExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Beagle.initialize(
                application = this,
                appearance = Appearance(
                    themeResourceId = R.style.BeagleTheme
                )
            )
            Beagle.setModules(
                LabelModule(text = "Static"),
                AppInfoButtonModule(),
                TextModule(text = "Text 1"),
                TextModule(text = "Text 2"),
                TextModule(
                    text = "This is a text 3. It's also clickable!",
                    onItemSelected = { "Text 3 clicked!".toast() }
                ),
                TextModule(text = "Text 4"),
                LabelModule(text = "Interactive"),
                AnimationDurationSwitchModule(
                    shouldBePersisted = true,
                    onValueChanged = { "Slow down animations ${if (it) "ON" else "OFF"}".toast() }
                ),
                SwitchModule(
                    text = "Just a switch",
                    onValueChanged = { "Switch ${if (it) "ON" else "OFF"}".toast() }
                ),
                SwitchModule(
                    id = SHOULD_DRAW_CIRCLE_SWITCH_ID,
                    text = "Should draw circle (persisted)",
                    shouldBePersisted = true,
                    onValueChanged = { Beagle.invalidateOverlay() }
                ),
                ButtonModule(
                    text = "Toggle the switch above",
                    onButtonPressed = {
                        Beagle.findModule<SwitchModule>(SHOULD_DRAW_CIRCLE_SWITCH_ID)?.let { module ->
                            module.setCurrentValue(Beagle, module.getCurrentValue(Beagle) != true)
                        }
                    }
                ),
                ButtonModule(
                    text = "Button 1",
                    onButtonPressed = { "Button 1 pressed".toast() }
                ),
                CheckBoxModule(
                    text = "Checkbox 1",
                    onValueChanged = { "Checkbox 1 ${if (it) "ON" else "OFF"}".toast() }
                ),
                CheckBoxModule(
                    text = "Checkbox 2",
                    onValueChanged = { "Checkbox 2 ${if (it) "ON" else "OFF"}".toast() }
                ),
                ItemListModule(
                    title = "Expandable list",
                    items = (0..5).map { index ->
                        object : BeagleListItemContract {
                            override val id = "item_$index"
                            override val text = "Item $index"
                        }
                    },
                    onItemSelected = { itemId -> "$itemId selected".toast() }
                ),
                SingleSelectionListModule(
                    title = "Single selection list",
                    items = (0..5).map { index ->
                        object : BeagleListItemContract {
                            override val id = "item_$index"
                            override val text = "Item $index"
                        }
                    },
                    initialValue = "item_2",
                    onValueChanged = { itemId -> "$itemId selected".toast() }
                ),
                MultipleSelectionListModule(
                    id = "jewrewr",
                    title = "Multiple selection list",
                    items = (0..6).map { index ->
                        object : BeagleListItemContract {
                            override val id = "item_$index"
                            override val text = "Item $index"
                        }
                    },
                    shouldBePersisted = true,
                    initialValue = emptySet(),
                    onValueChanged = { itemIds -> "${itemIds.size} items selected".toast() }
                )
            )
            Beagle.addOverlayListener(object : OverlayListener {

                private val paint = Paint().apply {
                    style = Paint.Style.FILL
                    color = Color.CYAN
                }

                override fun onDrawOver(canvas: Canvas) {
                    if (Beagle.findModule<SwitchModule>(SHOULD_DRAW_CIRCLE_SWITCH_ID)?.getCurrentValue(Beagle) == true) {
                        canvas.drawCircle(100f, 100f, 50f, paint)
                    }
                }
            })
        }
    }

    private fun String.toast() = Toast.makeText(this@BeagleExampleApplication, this, Toast.LENGTH_SHORT).show()

    companion object {
        private const val SHOULD_DRAW_CIRCLE_SWITCH_ID = "shouldDrawCircle"
    }
}