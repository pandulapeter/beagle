package com.pandulapeter.beagle.core.view.networkLogDetail.list

internal interface NetworkLogDetailListItem {

    val lineIndex: Int

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}