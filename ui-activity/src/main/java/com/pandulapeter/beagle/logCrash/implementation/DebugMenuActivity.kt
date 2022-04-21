package com.pandulapeter.beagle.logCrash.implementation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.WindowInsetsCompat
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.common.configuration.getBeagleInsets
import com.pandulapeter.beagle.core.view.InternalDebugMenuView
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class DebugMenuActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationOverlay: View
    private lateinit var debugMenu: InternalDebugMenuView

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beagle_activity_debug_menu)
        supportActionBar?.hide()
        toolbar = findViewById(R.id.beagle_toolbar)
        bottomNavigationOverlay = findViewById(R.id.beagle_bottom_navigation_overlay)
        debugMenu = findViewById(R.id.beagle_debug_menu)
        toolbar.run {
            setNavigationOnClickListener { onBackPressed() }
            navigationIcon = tintedDrawable(R.drawable.beagle_ic_close, colorResource(android.R.attr.textColorPrimary))
        }
        bottomNavigationOverlay.setBackgroundColor(window.navigationBarColor)
        window.decorView.run {
            setOnApplyWindowInsetsListener { view, insets ->
                onApplyWindowInsets(insets).also {
                    val input = WindowInsetsCompat.toWindowInsetsCompat(it, view).getBeagleInsets(WindowInsetsCompat.Type.systemBars())
                    val output = BeagleCore.implementation.appearance.applyInsets?.invoke(input) ?: input.copy(top = 0)
                    toolbar.setPadding(output.left, 0, output.right, 0)
                    debugMenu.applyInsets(0, 0, 0, output.bottom)
                    bottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = output.bottom } }
                }
            }
            requestApplyInsets()
        }
        if (savedInstanceState == null) {
            BeagleCore.implementation.notifyVisibilityListenersOnShow()
        }
    }

    override fun onStart() {
        super.onStart()
        (BeagleCore.implementation.uiManager as ActivityUiManager).debugMenuActivity = this
    }

    override fun onStop() {
        (BeagleCore.implementation.uiManager as ActivityUiManager).let {
            if (it.debugMenuActivity == this) {
                it.debugMenuActivity = null
                if (isFinishing) {
                    BeagleCore.implementation.notifyVisibilityListenersOnHide()
                }
            }
        }
        super.onStop()
    }
}