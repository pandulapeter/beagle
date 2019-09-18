package com.pandulapeter.debugMenuExample

import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.debugMenu.DebugMenu

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onBackPressed() {
        if (!DebugMenu.closeDrawerIfOpen(this)) {
            super.onBackPressed()
        }
    }
}