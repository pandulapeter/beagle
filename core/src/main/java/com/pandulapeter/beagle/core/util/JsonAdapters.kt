package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import com.pandulapeter.beagle.core.util.model.LogEntry
import com.pandulapeter.beagle.core.util.model.RestoreModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

private val moshi by lazy { Moshi.Builder().build() }

internal val logEntryAdapter: JsonAdapter<LogEntry> by lazy { moshi.adapter(LogEntry::class.java) }

val crashLogEntryAdapter: JsonAdapter<CrashLogEntry> by lazy { moshi.adapter(CrashLogEntry::class.java) }

val restoreModelAdapter: JsonAdapter<RestoreModel> by lazy { moshi.adapter(RestoreModel::class.java) }