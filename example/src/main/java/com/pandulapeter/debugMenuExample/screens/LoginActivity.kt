package com.pandulapeter.debugMenuExample.screens

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuCore.configuration.modules.AuthenticationHelperModule
import com.pandulapeter.debugMenuExample.R
import com.pandulapeter.debugMenuExample.utils.mockAccounts

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.back_button).setOnClickListener { supportFinishAfterTransition() }
    }

    override fun onResume() {
        super.onResume()
        DebugMenu.moduleConfiguration = DebugMenu.moduleConfiguration.copy(
            authenticationHelperModule = AuthenticationHelperModule(
                accounts = mockAccounts,
                onAccountSelected = { account ->
                    findViewById<EditText>(R.id.username_input).setText(account.first)
                    findViewById<EditText>(R.id.password_input).setText(account.second)
                    DebugMenu.closeDrawer(this)
                }
            )
        )
    }

    override fun onPause() {
        super.onPause()
        DebugMenu.moduleConfiguration = DebugMenu.moduleConfiguration.copy(authenticationHelperModule = null)
    }

    override fun onBackPressed() {
        if (!DebugMenu.closeDrawer(this)) {
            super.onBackPressed()
        }
    }
}