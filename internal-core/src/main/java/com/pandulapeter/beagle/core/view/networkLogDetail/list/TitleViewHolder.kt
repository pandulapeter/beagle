package com.pandulapeter.beagle.core.view.networkLogDetail.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.core.databinding.BeagleItemNetworkLogDetailTitleBinding
import com.pandulapeter.beagle.utils.extensions.inflater

internal class TitleViewHolder private constructor(
    private val binding: BeagleItemNetworkLogDetailTitleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiModel: UiModel) {
        binding.beagleTextView.text = uiModel.title
    }

    data class UiModel(
        val title: String
    ) : NetworkLogDetailListItem {

        override val lineIndex = -100
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = TitleViewHolder(
            BeagleItemNetworkLogDetailTitleBinding.inflate(parent.inflater, parent, false)
        )
    }
}