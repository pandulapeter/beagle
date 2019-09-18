package com.pandulapeter.debugMenuCore

import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.pandulapeter.debugMenuCore.modules.DebugMenuModule

data class DebugMenuConfiguration(
    @ColorInt val  backgroundColor: Int? = null,
    @Dimension val drawerWidth: Int? = null,
    val modules: List<DebugMenuModule> = emptyList()
)