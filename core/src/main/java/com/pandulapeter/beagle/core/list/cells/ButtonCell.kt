package com.pandulapeter.beagle.core.list.cells

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.common.contracts.module.ViewHolderDelegate
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.dimension

internal data class ButtonCell(
    override val id: String,
    private val text: CharSequence,
    @ColorInt private val color: Int?,
    private val onButtonPressed: () -> Unit
) : Cell<ButtonCell> {

    override fun createViewHolderDelegate() = object : ViewHolderDelegate<ButtonCell> {

        override val cellType = ButtonCell::class

        override fun createViewHolder(parent: ViewGroup) = ButtonViewHolder(parent.context)
    }

    private class ButtonViewHolder(context: Context) : ViewHolder<ButtonCell>(AppCompatButton(context)) {

        init {
            context.dimension(R.dimen.beagle_content_padding).let { padding ->
                itemView.setPadding(padding, padding, padding, padding)
            }
        }

        override fun bind(model: ButtonCell) {
            (itemView as TextView).run {
                text = model.text
                model.color?.let { setTextColor(it) }
                itemView.setOnClickListener {
                    adapterPosition.let { adapterPosition ->
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            model.onButtonPressed()
                        }
                    }
                }
            }
        }
    }
}