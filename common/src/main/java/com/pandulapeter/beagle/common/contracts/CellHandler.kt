package com.pandulapeter.beagle.common.contracts

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

/**
 * TODO
 */
interface CellHandler<T : Cell> {

    val cell: KClass<T>

    fun createViewHolder(parent: ViewGroup): ViewHolder<T>

    abstract class ViewHolder<T : Cell>(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun bind(model:  T)
    }
}

  