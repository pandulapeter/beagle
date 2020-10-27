package com.pandulapeter.beagle.core.util

import com.squareup.moshi.Moshi

private val moshi by lazy { Moshi.Builder().build() }

internal val logEntryAdapter by lazy { moshi.adapter(LogEntry::class.java) }

internal val crashLogEntryAdapter by lazy { moshi.adapter(CrashLogEntry::class.java) }