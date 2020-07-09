package com.pandulapeter.beagleExample.screens

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagleCore.configuration.Positioning
import com.pandulapeter.beagleCore.configuration.Trick
import com.pandulapeter.beagleCore.contracts.BeagleListener
import com.pandulapeter.beagleExample.R
import com.pandulapeter.beagleExample.utils.mockTestAccounts
import com.pandulapeter.beagleExample.utils.showKeyboard

@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.open_debug_menu_button).setOnClickListener { Beagle.fetch() }
        findViewById<View>(R.id.back_button).setOnClickListener { supportFinishAfterTransition() }
        Beagle.learn(
            trick = Trick.LoremIpsumButton(
                editTextId = R.id.message
            ),
            positioning = Positioning.Below(Trick.ForceCrashButton.ID),
            lifecycleOwner = this
        )
        Beagle.learn(
            trick = Trick.SimpleList(
                title = "Test accounts",
                items = mockTestAccounts,
                isInitiallyExpanded = true,
                onItemSelected = { account ->
                    findViewById<EditText>(R.id.username_input).setText(account.name)
                    findViewById<EditText>(R.id.password_input).setText(account.password)
                    Beagle.dismiss()
                }
            ),
            positioning = Positioning.Below(Trick.LoremIpsumButton.ID),
            lifecycleOwner = this
        )
        Beagle.addListener(this, object : BeagleListener {

            override fun onDrawerOpened() = Toast.makeText(this@LoginActivity, "Opened", Toast.LENGTH_SHORT).show()

            override fun onDrawerClosed() = Toast.makeText(this@LoginActivity, "Closed", Toast.LENGTH_SHORT).show()

            override fun onDrawerDragStarted() = Toast.makeText(this@LoginActivity, "Drag started", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onResume() {
        super.onResume()
        findViewById<View>(R.id.username_input).run { postDelayed({ showKeyboard() }, 300 ) }
    }
}