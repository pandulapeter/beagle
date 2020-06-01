package com.pandulapeter.beagle.core.list.moduleCells

import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.pandulapeter.beagle.common.contracts.ModuleCell
import com.pandulapeter.beagle.common.contracts.ViewHolderDelegate

internal data class TextModuleCell(
    override val id: String,
    val text: CharSequence
) : ModuleCell<TextModuleCell> {

    override fun createViewHolderDelegate() = object : ViewHolderDelegate<TextModuleCell> {

        override val cellType = TextModuleCell::class

        override fun createViewHolder(parent: ViewGroup) = object : ViewHolderDelegate.ViewHolder<TextModuleCell>(AppCompatTextView(parent.context).apply {
            text = this.text
        }) {
            override fun bind(model: TextModuleCell) {
                (itemView as TextView).text = model.text
            }
        }
    }
}