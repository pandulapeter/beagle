package com.pandulapeter.beagle.appExample.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appExample.R
import com.pandulapeter.beagle.appExample.utils.showKeyboard
import com.pandulapeter.beagle.common.contracts.VisibilityListener

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.open_debug_menu_button).setOnClickListener { Beagle.show() }
        findViewById<View>(R.id.back_button).setOnClickListener { supportFinishAfterTransition() }
        Beagle.addVisibilityListener(
            listener = object : VisibilityListener {

                override fun onShown() = Toast.makeText(this@LoginActivity, "Debug menu shown", Toast.LENGTH_SHORT).show()

                override fun onHidden() = Toast.makeText(this@LoginActivity, "Debug menu hidden", Toast.LENGTH_SHORT).show()
            },
            lifecycleOwner = this
        )
    }

    override fun onResume() {
        super.onResume()
        findViewById<View>(R.id.username_input).run { postDelayed({ showKeyboard() }, 300) }
    }
}