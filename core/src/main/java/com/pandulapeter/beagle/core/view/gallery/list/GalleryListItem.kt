package com.pandulapeter.beagle.core.view.gallery.list

internal interface GalleryListItem {

    val id: String
    val lastModified: Long

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}