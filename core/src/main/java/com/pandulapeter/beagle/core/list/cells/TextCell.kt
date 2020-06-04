package com.pandulapeter.beagle.core.list.cells

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolderDelegate
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.dimension

internal data class TextCell(
    override val id: String,
    private val text: CharSequence,
    @ColorInt private val color: Int?
) : Cell<TextCell> {

    override fun createViewHolderDelegate() = object : ViewHolderDelegate<TextCell> {

        override val cellType = TextCell::class

        override fun createViewHolder(parent: ViewGroup) =
            object : ViewHolderDelegate.ViewHolder<TextCell>(AppCompatTextView(parent.context)) {

                init {
                    itemView.context.dimension(R.dimen.beagle_content_padding).let { padding ->
                        itemView.setPadding(padding, padding, padding, padding)
                    }
                }

                override fun bind(model: TextCell) {
                    (itemView as TextView).run {
                        text = model.text
                        model.color?.let { setTextColor(it) }
                    }
                }
            }
    }
}