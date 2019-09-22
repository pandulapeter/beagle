package com.pandulapeter.debugMenuExample.screens

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.configuration.modules.ListModule
import com.pandulapeter.debugMenuExample.R
import com.pandulapeter.debugMenuExample.utils.mockAccounts

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.open_debug_menu_button).setOnClickListener { DebugMenu.openDrawer(this) }
        findViewById<View>(R.id.back_button).setOnClickListener { supportFinishAfterTransition() }
    }

    override fun onResume() {
        super.onResume()
        //TODO: Come up with a nicer API for this functionality.
        DebugMenu.modules = DebugMenu.modules.toMutableList().apply {
            add(3, ListModule(
                id = TEST_ACCOUNTS_MODULE_ID,
                title = "Test accounts",
                items = mockAccounts,
                onItemSelected = { id ->
                    mockAccounts.firstOrNull { it.id == id }?.let { account ->
                        findViewById<EditText>(R.id.username_input).setText(account.username)
                        findViewById<EditText>(R.id.password_input).setText(account.password)
                    }
                    DebugMenu.closeDrawer(this@LoginActivity)
                }
            ))
        }
    }

    override fun onPause() {
        super.onPause()
        //TODO: Come up with a nicer API for this functionality.
        DebugMenu.modules = DebugMenu.modules.filterNot { it.id == TEST_ACCOUNTS_MODULE_ID }
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