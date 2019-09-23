package com.pandulapeter.debugMenuExample

import android.app.Application
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.configuration.modules.LongTextModule
import com.pandulapeter.debugMenuCore.configuration.modules.TextModule

@Suppress("unused")
class DebugMenuExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DebugMenu.attachToApplication(this)
        DebugMenu.setModules(
            listOf(
                LongTextModule(
                    title = "LongTextModule 1",
                    text = "Long text"
                ),
                LongTextModule(
                    title = "LongTextModule 2",
                    text = "The difference between Text and LongText is that LongText can be expanded and collapsed by the user."
                ),
                LongTextModule(
                    title = "LongTextModule 3",
                    text = ""
                )
            )
        )
    }

    private fun String.showToast() = Toast.makeText(this@DebugMenuExampleApplication, this, Toast.LENGTH_SHORT).show()
}