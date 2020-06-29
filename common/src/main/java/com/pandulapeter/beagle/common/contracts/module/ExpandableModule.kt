package com.pandulapeter.beagle.common.contracts.module

/**
 * Modules that can be expanded / collapsed should implement this interface.
 *
 * @param M - The type of the module.
 */
interface ExpandableModule<M : Module<M>> : Module<M> {

    /**
     * The title of the module that will be displayed in the header of the list.
     */
    val title: CharSequence

    /**
     *  Whether or not the list is expanded the first time the module becomes visible.
     */
    val isExpandedInitially: Boolean
}