package com.pandulapeter.debugMenu

import android.util.Log
import com.pandulapeter.debugMenuCore.DebugMenu

object DebugMenu : DebugMenu {

    override fun initialize() {
        Log.d("DEBUG_MENU", "Real initialization")
    }
}