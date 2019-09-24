package com.pandulapeter.debugMenu.utils

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

internal fun Context.animatedDrawable(@DrawableRes drawableId: Int) = AnimatedVectorDrawableCompat.create(this, drawableId)

@ColorInt
internal fun Context.colorResource(@AttrRes id: Int): Int {
    val resolvedAttr = TypedValue()
    theme.resolveAttribute(id, resolvedAttr, true)
    val colorRes = resolvedAttr.run { if (resourceId != 0) resourceId else data }
    return ContextCompat.getColor(this, colorRes)
}

internal fun Context.dimension(@DimenRes dimensionResInt: Int) = resources.getDimensionPixelSize(dimensionResInt)

internal fun Context.drawable(@DrawableRes drawableresId: Int) = AppCompatResources.getDrawable(this, drawableresId)

internal fun View.hideKeyboard() {
    clearFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
}

internal inline fun <T : Fragment> T.withArguments(bundleOperations: (Bundle) -> Unit): T = apply {
    arguments = Bundle().apply { bundleOperations(this) }
}

internal fun View.setBackgroundFromWindowBackground() {
    val typedValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
    if (typedValue.type >= TypedValue.TYPE_FIRST_COLOR_INT && typedValue.type <= TypedValue.TYPE_LAST_COLOR_INT) {
        setBackgroundColor(typedValue.data)
    } else {
        background = context.drawable(typedValue.resourceId)
    }
}

internal var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }