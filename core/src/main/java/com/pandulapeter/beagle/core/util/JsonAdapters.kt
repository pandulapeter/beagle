package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import com.pandulapeter.beagle.core.util.model.LogEntry
import com.pandulapeter.beagle.core.util.model.RestoreModel
import com.squareup.moshi.Moshi

private val moshi by lazy { Moshi.Builder().build() }

internal val logEntryAdapter by lazy { moshi.adapter(LogEntry::class.java) }

internal val crashLogEntryAdapter by lazy { moshi.adapter(CrashLogEntry::class.java) }

internal val restoreModelAdapter by lazy { moshi.adapter(RestoreModel::class.java) }