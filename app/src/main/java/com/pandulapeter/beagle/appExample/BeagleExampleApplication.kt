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
import com.pandulapeter.beagle.modules.CheckboxModule
import com.pandulapeter.beagle.modules.LabelModule
import com.pandulapeter.beagle.modules.ItemListModule
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
                TextModule(
                    text = "This is a green text",
                    color = Color.GREEN
                ),
                TextModule(
                    text = "This is a red text",
                    color = Color.RED
                ),
                TextModule(
                    text = "This is a blue text. It's also clickable!",
                    color = Color.BLUE,
                    onItemSelected = { "Blue text clicked!".toast() }
                ),
                TextModule(
                    text = "This text uses the default color"
                ),
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
                    color = Color.YELLOW,
                    onButtonPressed = { "Button 1 pressed".toast() }
                ),
                CheckboxModule(
                    text = "Checkbox 1",
                    onValueChanged = { "Checkbox 1 ${if (it) "ON" else "OFF"}".toast() }
                ),
                CheckboxModule(
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