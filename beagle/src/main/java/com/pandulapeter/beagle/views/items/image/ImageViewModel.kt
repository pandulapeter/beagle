package com.pandulapeter.beagle.views.items.image

import android.graphics.drawable.Drawable
import com.pandulapeter.beagle.views.items.DrawerItemViewModel

internal data class ImageViewModel(
    override val id: String,
    val drawable: Drawable?
) : DrawerItemViewModel