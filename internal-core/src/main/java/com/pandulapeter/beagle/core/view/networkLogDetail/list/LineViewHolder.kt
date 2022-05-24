package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleItemNetworkLogDetailLineBinding
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable
import kotlin.math.max

internal class LineViewHolder private constructor(
    private val binding: BeagleItemNetworkLogDetailLineBinding,
    onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val drawableExpand by lazy { binding.root.context.tintedDrawable(R.drawable.beagle_ic_expand, binding.beagleTextView.textColors.defaultColor) }
    private val drawableCollapse by lazy { binding.root.context.tintedDrawable(R.drawable.beagle_ic_collapse, binding.beagleTextView.textColors.defaultColor) }
    private val drawableEmpty by lazy { binding.root.context.tintedDrawable(R.drawable.beagle_ic_empty, binding.beagleTextView.textColors.defaultColor) }
    private val largeContentPadding = binding.root.context.dimension(R.dimen.beagle_large_content_padding)

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                (binding.beagleTextView.tag as? Int?)?.let { lineNumber ->
                    onItemClicked(lineNumber)
                }
            }
        }
    }

    fun bind(uiModel: UiModel) = binding.beagleTextView.run {
        tag = uiModel.lineIndex
        text = uiModel.content
        isClickable = uiModel.isClickable
        setCompoundDrawablesWithIntrinsicBounds(
            if (uiModel.isClickable) {
                if (uiModel.isCollapsed) drawableExpand else drawableCollapse
            } else if (uiModel.hasCollapsingContent) drawableEmpty else null,
            null, null, null
        )
        setPadding(
            largeContentPadding + if (uiModel.hasCollapsingContent) max(largeContentPadding * uiModel.level, 0) else largeContentPadding * uiModel.level,
            paddingTop,
            largeContentPadding * if (uiModel.isClickable) 2 else 1,
            paddingBottom
        )
    }

    data class UiModel(
        override val lineIndex: Int,
        val content: CharSequence,
        val level: Int,
        val hasCollapsingContent: Boolean,
        val isClickable: Boolean,
        val isCollapsed: Boolean
    ) : NetworkLogDetailListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: (Int) -> Unit
        ) = LineViewHolder(
            binding = BeagleItemNetworkLogDetailLineBinding.inflate(parent.inflater, parent, false),
            onItemClicked = onItemClicked
        )
    }
}