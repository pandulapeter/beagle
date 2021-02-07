package com.pandulapeter.beagle.core.util

import com.pandulapeter.beagle.core.util.model.SerializableCrashLogEntry
import com.pandulapeter.beagle.core.util.model.SerializableLogEntry
import com.pandulapeter.beagle.core.util.model.RestoreModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

private val moshi by lazy { Moshi.Builder().build() }

internal val logEntryAdapter: JsonAdapter<SerializableLogEntry> by lazy { moshi.adapter(SerializableLogEntry::class.java) }

val crashLogEntryAdapter: JsonAdapter<SerializableCrashLogEntry> by lazy { moshi.adapter(SerializableCrashLogEntry::class.java) }

val restoreModelAdapter: JsonAdapter<RestoreModel> by lazy { moshi.adapter(RestoreModel::class.java) }