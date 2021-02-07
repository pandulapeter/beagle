package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.databinding.BeagleItemNetworkLogDetailMetadataHeaderBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.inflater
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class MetadataHeaderViewHolder private constructor(
    private val binding: BeagleItemNetworkLogDetailMetadataHeaderBinding,
    onItemClicked: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val drawableExpand by lazy { binding.root.context.tintedDrawable(R.drawable.beagle_ic_expand, binding.beagleTextView.textColors.defaultColor) }
    private val drawableCollapse by lazy { binding.root.context.tintedDrawable(R.drawable.beagle_ic_collapse, binding.beagleTextView.textColors.defaultColor) }

    init {
        binding.root.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onItemClicked()
            }
        }
    }

    fun bind(uiModel: UiModel) = binding.beagleTextView.run {
        setText(BeagleCore.implementation.appearance.networkLogTexts.metadata)
        setCompoundDrawablesWithIntrinsicBounds(if (uiModel.isCollapsed) drawableExpand else drawableCollapse, null, null, null)
    }

    data class UiModel(
        val isCollapsed: Boolean
    ) : NetworkLogDetailListItem {

        override val lineIndex = -300
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onItemClicked: () -> Unit
        ) = MetadataHeaderViewHolder(
            binding = BeagleItemNetworkLogDetailMetadataHeaderBinding.inflate(parent.inflater, parent, false),
            onItemClicked = onItemClicked
        )
    }
}