package com.pandulapeter.beagle.appExample.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appExample.R

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.open_debug_menu_button).setOnClickListener { Beagle.show() }
        findViewById<View>(R.id.open_login_screen_button).setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
    }
}