package com.pandulapeter.debugMenuExample.screens

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.ModulePositioning
import com.pandulapeter.debugMenuCore.configuration.modules.ListModule
import com.pandulapeter.debugMenuCore.configuration.modules.ScreenshotButtonModule
import com.pandulapeter.debugMenuExample.R
import com.pandulapeter.debugMenuExample.utils.mockAccounts

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.open_debug_menu_button).setOnClickListener { DebugMenu.openDrawer(this) }
        findViewById<View>(R.id.back_button).setOnClickListener { supportFinishAfterTransition() }
    }

    override fun onStart() {
        super.onStart()
        DebugMenu.putModule(
            module = ListModule(
                id = TEST_ACCOUNTS_MODULE_ID,
                title = "Test accounts",
                items = mockAccounts,
                onItemSelected = { account ->
                    findViewById<EditText>(R.id.username_input).setText(account.name)
                    findViewById<EditText>(R.id.password_input).setText(account.password)
                    DebugMenu.closeDrawer(this@LoginActivity)
                }
            ),
            positioning = ModulePositioning.Below(ScreenshotButtonModule.ID)
        )
    }

    override fun onStop() {
        super.onStop()
        DebugMenu.removeModule(TEST_ACCOUNTS_MODULE_ID)
    }

    override fun onBackPressed() {
        if (!DebugMenu.closeDrawer(this)) {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TEST_ACCOUNTS_MODULE_ID = "testAccounts"
    }
}