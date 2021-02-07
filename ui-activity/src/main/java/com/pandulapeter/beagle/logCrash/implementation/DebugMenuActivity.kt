package com.pandulapeter.beagle.logCrash.implementation

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.R
import com.pandulapeter.beagle.common.configuration.Insets
import com.pandulapeter.beagle.databinding.BeagleActivityDebugMenuBinding
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class DebugMenuActivity : AppCompatActivity() {

    private lateinit var binding: BeagleActivityDebugMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.beagle_activity_debug_menu)
        supportActionBar?.hide()
        binding.beagleToolbar.run {
            setNavigationOnClickListener { onBackPressed() }
            navigationIcon = tintedDrawable(R.drawable.beagle_ic_close, colorResource(android.R.attr.textColorPrimary))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            binding.beagleBottomNavigationOverlay.setBackgroundColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor else Color.BLACK)
            window.decorView.run {
                setOnApplyWindowInsetsListener { _, insets ->
                    onApplyWindowInsets(insets).also {
                        val input = Insets(
                            left = it.systemWindowInsetLeft,
                            top = it.systemWindowInsetTop,
                            right = it.systemWindowInsetRight,
                            bottom = it.systemWindowInsetBottom
                        )
                        val output = BeagleCore.implementation.appearance.applyInsets?.invoke(input) ?: Insets(
                            left = it.systemWindowInsetLeft,
                            top = 0,
                            right = it.systemWindowInsetRight,
                            bottom = it.systemWindowInsetBottom
                        )
                        binding.beagleToolbar.setPadding(output.left, 0, output.right, 0)
                        binding.beagleDebugMenu.applyInsets(0, 0, 0, output.bottom)
                        binding.beagleBottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = output.bottom } }
                    }
                }
                requestApplyInsets()
            }
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