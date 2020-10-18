package com.pandulapeter.beagle.core.view.bugReport.list

internal interface BugReportListItem {

    val id: String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}