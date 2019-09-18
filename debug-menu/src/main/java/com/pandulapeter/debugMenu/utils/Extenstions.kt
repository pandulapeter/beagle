package com.pandulapeter.debugMenu.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat

internal fun Context.color(@ColorRes colorResInt: Int) = ContextCompat.getColor(this, colorResInt)

internal fun Context.dimension(@DimenRes dimensionResInt: Int) = resources.getDimensionPixelSize(dimensionResInt)