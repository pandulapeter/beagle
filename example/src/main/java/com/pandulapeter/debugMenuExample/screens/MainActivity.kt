package com.pandulapeter.debugMenuExample.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.debugMenu.DebugMenu
import com.pandulapeter.debugMenuExample.R
import com.pandulapeter.debugMenuExample.networking.NetworkingManager
import com.pandulapeter.debugMenuExample.utils.executeRequest
import com.pandulapeter.debugMenuExample.utils.logMessages

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.open_debug_menu_button).setOnClickListener { DebugMenu.openDrawer(this) }
        findViewById<View>(R.id.generate_music_genre_button).setOnClickListener { generateMusicGenre() }
        findViewById<View>(R.id.add_log_message_button).setOnClickListener { DebugMenu.log(message = logMessages.random(), payload = listOf("Random payload", null).random()) }
        findViewById<View>(R.id.open_login_screen_button).setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
    }

    override fun onBackPressed() {
        if (!DebugMenu.closeDrawer(this)) {
            super.onBackPressed()
        }
    }

    private fun generateMusicGenre() = NetworkingManager.musicGeneratorService.generateMusicGenre().executeRequest(
        onSuccess = { musicGenre -> Toast.makeText(this, musicGenre, Toast.LENGTH_SHORT).show() },
        onError = { Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show() }
    )
}