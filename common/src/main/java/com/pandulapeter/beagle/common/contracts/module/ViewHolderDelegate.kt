package com.pandulapeter.beagle.common.contracts.module

import android.view.ViewGroup

/**
 * Connects the conceptual [Cell] representation of a UI element to the actual View responsible for displaying it.
 */
interface ViewHolderDelegate<T : Cell<T>> {

    /**
     * Returns a new [ViewHolder] instance, specific to the [Cell] type.
     */
    fun createViewHolder(parent: ViewGroup): ViewHolder<T>
}