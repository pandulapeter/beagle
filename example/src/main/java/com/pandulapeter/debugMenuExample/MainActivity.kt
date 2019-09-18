package com.pandulapeter.debugMenuExample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.debugMenu.DebugMenu

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.open_debug_menu_button).setOnClickListener { DebugMenu.openDrawer(this) }
    }

    override fun onBackPressed() {
        if (!DebugMenu.closeDrawer(this)) {
            super.onBackPressed()
        }
    }
}