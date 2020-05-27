package com.pandulapeter.beagleExample.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagleExample.R
import com.pandulapeter.beagleExample.utils.showKeyboard

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.open_debug_menu_button).setOnClickListener { Beagle.show() }
        findViewById<View>(R.id.back_button).setOnClickListener { supportFinishAfterTransition() }
    }

    override fun onResume() {
        super.onResume()
        findViewById<View>(R.id.username_input).run { postDelayed({ showKeyboard() }, 300) }
    }
}