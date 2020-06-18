package com.pandulapeter.beagle.appDemo.feature.shared.list

interface ListItem {

    val id: String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}