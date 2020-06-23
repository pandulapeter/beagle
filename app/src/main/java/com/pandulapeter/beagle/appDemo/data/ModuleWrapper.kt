package com.pandulapeter.beagle.appDemo.data

import androidx.annotation.StringRes
import com.pandulapeter.beagle.common.contracts.module.Module
import java.util.UUID

data class ModuleWrapper(
    val id: String = UUID.randomUUID().toString(),
    @StringRes val titleResourceId: Int,
    val module: Module<*>
)