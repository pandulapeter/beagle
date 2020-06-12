package com.pandulapeter.beagle.common.contracts.module

import android.view.View
import android.view.ViewGroup
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.RecyclerView

/**
 * Enforces a bind method for the standard [RecyclerView.ViewHolder].
 */
abstract class ViewHolder<T : Cell<T>>(view: View) : RecyclerView.ViewHolder(view) {

    /**
     * Called when the UI needs to be updated with new data represented by a [Cell] instance.
     * Make sure to re-bind everything that is contained in the model.
     *
     * @param model - The [Cell] implementation to bind.
     */
    abstract fun bind(model: T)

    /**
     * For internal use only.
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @Suppress("UNCHECKED_CAST")
    fun forceBind(model: Cell<*>) = try {
        bind(model as T)
    } catch (_: ClassCastException) {
    }

    /**
     * Connects the conceptual [Cell] representation of a UI element to the actual View responsible for displaying it.
     * TODO: Can't this be replaced with a simple lambda function?
     */
    abstract class Delegate<T : Cell<T>> {

        /**
         * Returns a new [ViewHolder] instance, specific to the [Cell] type.
         */
        abstract fun createViewHolder(parent: ViewGroup): ViewHolder<T>
    }
}