package com.pandulapeter.beagleCore.implementation

@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
data class PendingChangeEvent(
    val trickId: String,
    val apply: () -> Unit,
    val reset: () -> Unit
)