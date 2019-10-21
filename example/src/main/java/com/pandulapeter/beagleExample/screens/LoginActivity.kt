package com.pandulapeter.beagleExample.screens

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagleCore.configuration.Positioning
import com.pandulapeter.beagleCore.configuration.Trick
import com.pandulapeter.beagleExample.R
import com.pandulapeter.beagleExample.utils.mockTestAccounts

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.open_debug_menu_button).setOnClickListener { Beagle.fetch(this) }
        findViewById<View>(R.id.back_button).setOnClickListener { supportFinishAfterTransition() }
    }

    override fun onStart() {
        super.onStart()
        Beagle.learn(
            trick = Trick.SimpleList(
                id = TEST_ACCOUNTS_MODULE_ID,
                title = "Test accounts",
                items = mockTestAccounts,
                isInitiallyExpanded = true,
                onItemSelected = { account ->
                    findViewById<EditText>(R.id.username_input).setText(account.name)
                    findViewById<EditText>(R.id.password_input).setText(account.password)
                    Beagle.dismiss(this@LoginActivity)
                }
            ),
            positioning = Positioning.Below(Trick.ForceCrashButton.ID)
        )
    }

    override fun onStop() {
        super.onStop()
        Beagle.forget(TEST_ACCOUNTS_MODULE_ID)
    }

    override fun onBackPressed() {
        if (!Beagle.dismiss(this)) {
            super.onBackPressed()
        }
    }

    companion object {
        private const val TEST_ACCOUNTS_MODULE_ID = "testAccounts"
    }
}