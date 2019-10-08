package com.pandulapeter.beagle.views.drawerItems.padding

import androidx.annotation.Dimension
import com.pandulapeter.beagle.views.drawerItems.DrawerItemViewModel

internal data class PaddingViewModel(
    override val id: String,
    @Dimension val size: Int?
) : DrawerItemViewModel