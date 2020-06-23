package com.pandulapeter.beagle.appDemo.feature.main.playground.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemPlaygroundGenerateCodeBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class GenerateCodeViewHolder private constructor(
    binding: ItemPlaygroundGenerateCodeBinding,
    onGenerateCodeClicked: () -> Unit
) : BaseViewHolder<ItemPlaygroundGenerateCodeBinding, GenerateCodeViewHolder.UiModel>(binding) {

    init {
        itemView.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                onGenerateCodeClicked()
            }
        }
    }

    data class UiModel(
        override val id: String = "generateCode"
    ) : PlaygroundListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onGenerateCodeClicked: () -> Unit
        ) = GenerateCodeViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_playground_generate_code, parent, false),
            onGenerateCodeClicked = onGenerateCodeClicked
        )
    }
}