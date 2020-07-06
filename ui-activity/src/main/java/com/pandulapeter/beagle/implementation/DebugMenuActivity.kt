package com.pandulapeter.beagle.implementation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.DebugMenuView
import com.pandulapeter.beagle.R

internal class DebugMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beagle_activity_debug_menu)
        supportActionBar?.hide()
        findViewById<ImageView>(R.id.beagle_close_button).apply {
            setImageDrawable(tintedDrawable(R.drawable.beagle_ic_close, colorResource(android.R.attr.textColorPrimary)))
            setOnClickListener { onBackPressed() }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            val debugMenu = findViewById<DebugMenuView>(R.id.beagle_debug_menu)
            val bottomNavigationOverlay = findViewById<View>(R.id.beagle_bottom_navigation_overlay)
            bottomNavigationOverlay.setBackgroundColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor else Color.BLACK)
            window.decorView.run {
                setOnApplyWindowInsetsListener { _, insets ->
                    insets.also {
                        debugMenu.applyInsets(it.systemWindowInsetLeft, 0, it.systemWindowInsetRight, it.systemWindowInsetBottom)
                        bottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = it.systemWindowInsetBottom } }
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


    private fun Context.tintedDrawable(@DrawableRes drawableResourceId: Int, @ColorInt tint: Int) = AppCompatResources.getDrawable(this, drawableResourceId)?.let { drawable ->
        DrawableCompat.wrap(drawable.mutate()).apply {
            DrawableCompat.setTint(this, tint)
            DrawableCompat.setTintMode(this, PorterDuff.Mode.SRC_IN)
        }
    }

    @SuppressLint("ResourceAsColor")
    @ColorInt
    private fun Context.colorResource(@AttrRes id: Int): Int {
        val resolvedAttr = TypedValue()
        theme.resolveAttribute(id, resolvedAttr, true)
        return ContextCompat.getColor(this, resolvedAttr.run { if (resourceId != 0) resourceId else data })
    }

}