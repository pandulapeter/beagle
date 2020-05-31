package com.pandulapeter.beagle.common.modules.text

import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.pandulapeter.beagle.common.contracts.Cell
import com.pandulapeter.beagle.common.contracts.CellHandler

data class TextCell(
    override val id: String,
    val text: String
) : Cell {

    class TextCellHandler : CellHandler<TextCell> {

        override val cell = TextCell::class

        override fun createViewHolder(parent: ViewGroup) = object : CellHandler.ViewHolder<TextCell>(AppCompatTextView(parent.context).apply {
            text = this.text
        }) {
            override fun bind(model: TextCell) {
                (itemView as TextView).text = model.text
            }
        }
    }
}