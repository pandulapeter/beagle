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
        //TODO: Come up with a nicer API for this functionality.
        DebugMenu.modules = DebugMenu.modules.toMutableList().apply {
            add(AuthenticationHelperModule(
                accounts = mockAccounts,
                onAccountSelected = { account ->
                    findViewById<EditText>(R.id.username_input).setText(account.first)
                    findViewById<EditText>(R.id.password_input).setText(account.second)
                    DebugMenu.closeDrawer(this@LoginActivity)
                }
            ))
        }
    }

    override fun onPause() {
        super.onPause()
        //TODO: Come up with a nicer API for this functionality.
        DebugMenu.modules = DebugMenu.modules.toMutableList().apply { removeAll { it is AuthenticationHelperModule } }
    }

    override fun onBackPressed() {
        if (!DebugMenu.closeDrawer(this)) {
            super.onBackPressed()
        }
    }
}