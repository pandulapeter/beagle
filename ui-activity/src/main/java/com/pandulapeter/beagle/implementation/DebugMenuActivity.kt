package com.pandulapeter.beagle.implementation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.pandulapeter.beagle.BeagleCore
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
    }

    override fun onStart() {
        super.onStart()
        BeagleCore.implementation.notifyVisibilityListenersOnShow()
    }

    override fun onStop() {
        super.onStop()
        BeagleCore.implementation.notifyVisibilityListenersOnHide()
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