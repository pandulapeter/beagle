package com.pandulapeter.beagle.core.view.gallery.list

internal interface GalleryListItem {

    val id: String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}