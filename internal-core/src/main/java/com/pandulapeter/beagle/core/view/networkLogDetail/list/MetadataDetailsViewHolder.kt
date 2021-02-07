package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.databinding.BeagleItemNetworkLogDetailMetadataDetailsBinding
import com.pandulapeter.beagle.utils.extensions.inflater

internal class MetadataDetailsViewHolder private constructor(
    private val binding: BeagleItemNetworkLogDetailMetadataDetailsBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiModel: UiModel) {
        binding.beagleTextView.text = uiModel.metadata
    }

    data class UiModel(
        val metadata: CharSequence,
    ) : NetworkLogDetailListItem {

        override val lineIndex = -301
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = MetadataDetailsViewHolder(
            BeagleItemNetworkLogDetailMetadataDetailsBinding.inflate(parent.inflater, parent, false)
        )
    }
}