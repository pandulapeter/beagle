package com.pandulapeter.debugMenu.utils

import android.content.Context
import androidx.annotation.DimenRes

internal fun Context.dimension(@DimenRes dimensionResInt: Int) = resources.getDimensionPixelSize(dimensionResInt)