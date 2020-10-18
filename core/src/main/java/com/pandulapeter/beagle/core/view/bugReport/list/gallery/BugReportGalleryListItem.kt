package com.pandulapeter.beagle.core.view.bugReport.list.gallery

internal interface BugReportGalleryListItem {

    val id: String
    val lastModified: Long

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}