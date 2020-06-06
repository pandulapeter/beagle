package com.pandulapeter.beagle.appExample

import android.app.Application
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.Toast
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.common.configuration.Appearance
import com.pandulapeter.beagle.common.listeners.OverlayListener
import com.pandulapeter.beagle.modules.ButtonModule
import com.pandulapeter.beagle.modules.CheckboxModule
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
                TextModule(
                    text = "This is a green text",
                    color = Color.GREEN
                ),
                TextModule(
                    text = "This is a red text",
                    color = Color.RED
                ),
                TextModule(
                    text = "This is a blue text",
                    color = Color.BLUE
                ),
                TextModule(
                    text = "This text uses the default color"
                ),
                SwitchModule(
                    id = SHOULD_DRAW_CIRCLE_SWITCH_ID,
                    text = "Should draw circle",
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
                    onButtonPressed = { Toast.makeText(this, "Button 1 pressed", Toast.LENGTH_SHORT).show() }
                ),
                CheckboxModule(
                    text = "Checkbox 1",
                    onValueChanged = { Toast.makeText(this, "Checkbox 1 ${if (it) "ON" else "OFF"}", Toast.LENGTH_SHORT).show() }
                ),
                CheckboxModule(
                    text = "Checkbox 2",
                    onValueChanged = { Toast.makeText(this, "Checkbox 2 ${if (it) "ON" else "OFF"}", Toast.LENGTH_SHORT).show() }
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

    companion object {
        private const val SHOULD_DRAW_CIRCLE_SWITCH_ID = "shouldDrawCircle"
    }
}