package com.pandulapeter.beagle.core.list.moduleCells

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import com.pandulapeter.beagle.common.contracts.ModuleCell
import com.pandulapeter.beagle.common.contracts.ViewHolderDelegate
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.dimension

internal data class TextModuleCell(
    override val id: String,
    val text: CharSequence,
    @ColorInt val color: Int?
) : ModuleCell<TextModuleCell> {

    override fun createViewHolderDelegate() = object : ViewHolderDelegate<TextModuleCell> {

        override val cellType = TextModuleCell::class

        override fun createViewHolder(parent: ViewGroup) =
            object : ViewHolderDelegate.ViewHolder<TextModuleCell>(AppCompatTextView(parent.context)) {

                init {
                    itemView.context.dimension(R.dimen.beagle_content_padding).let { padding ->
                        itemView.setPadding(padding, padding, padding, padding)
                    }
                }

                override fun bind(model: TextModuleCell) {
                    (itemView as TextView).run {
                        text = model.text
                        model.color?.let { setTextColor(it) }
                    }
                }
            }
    }
}