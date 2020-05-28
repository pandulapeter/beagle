package com.pandulapeter.beagle.common.contracts

interface BeagleModuleItem {

    val id: String

    override fun equals(other: Any?): Boolean

    override fun hashCode(): Int
}