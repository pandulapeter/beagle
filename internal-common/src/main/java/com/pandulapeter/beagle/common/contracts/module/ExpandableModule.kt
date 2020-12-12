package com.pandulapeter.beagle.common.contracts.module

import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.BeagleContract

/**
 * Modules that can be expanded / collapsed should implement this interface.
 *
 * @param M - The type of the module.
 */
interface ExpandableModule<M : Module<M>> : Module<M> {

    /**
     *  Whether or not the list is expanded the first time the module becomes visible.
     */
    val isExpandedInitially: Boolean

    /**
     * The title of the module that will be displayed in the header of the list.
     */
    fun getHeaderTitle(beagle: BeagleContract): Text
}