package com.pandulapeter.beagle.common.contracts

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

/**
 * Connects the conceptual [Cell] representation of a UI element to the actual View responsible for displaying it.
 */
interface ViewHolderDelegate<T : Cell<T>> {

    /**
     * The type of the [Cell] implementation that this delegate is handling (T::class).
     */
    val cellType: KClass<T>

    /**
     * Returns a new [ViewHolder] instance, specific to the [Cell] type.
     */
    fun createViewHolder(parent: ViewGroup): ViewHolder<T>

    /**
     * Enforces a bind method for the standard [RecyclerView.ViewHolder].
     */
    abstract class ViewHolder<T : Cell<T>>(view: View) : RecyclerView.ViewHolder(view) {

        /**
         * Called when the UI needs to be updated with new data represented by a [Cell] instance.
         *
         * @param model - The [Cell] implementation to bind.
         */
        abstract fun bind(model: T)

        /**
         * For internal use only.
         */
        @Suppress("UNCHECKED_CAST")
        fun forceBind(model: Cell<*>) = bind(model as T)
    }
}