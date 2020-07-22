package com.pandulapeter.beagle.core.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.colorResource
import com.pandulapeter.beagle.core.util.extension.getScreenCapturesFolder
import com.pandulapeter.beagle.core.util.extension.tintedDrawable

internal class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beagle_activity_gallery)
        supportActionBar?.hide()
        findViewById<ImageView>(R.id.beagle_close_button).apply {
            setImageDrawable(tintedDrawable(R.drawable.beagle_ic_close, colorResource(android.R.attr.textColorPrimary)))
            setOnClickListener { onBackPressed() }
        }
        val textView = findViewById<TextView>(R.id.beagle_text_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            val bottomNavigationOverlay = findViewById<View>(R.id.beagle_bottom_navigation_overlay)
            bottomNavigationOverlay.setBackgroundColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor else Color.BLACK)
            window.decorView.run {
                setOnApplyWindowInsetsListener { _, insets ->
                    onApplyWindowInsets(insets).also {
                        textView.setPadding(it.systemWindowInsetLeft, 0, it.systemWindowInsetRight, it.systemWindowInsetBottom)
                        bottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = it.systemWindowInsetBottom } }
                    }
                }
                requestApplyInsets()
            }
        }
        //TODO:
        textView.text = getString(R.string.beagle_coming_soon) + "\n\n" + getScreenCapturesFolder().listFiles()?.joinToString(separator = "\n") { it.name } ?: ""
    }
}