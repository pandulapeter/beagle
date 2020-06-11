package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemCodeSnippetBinding
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationListItem

class CodeSnippetViewHolder private constructor(
    binding: ItemCodeSnippetBinding
) : BaseViewHolder<ItemCodeSnippetBinding, CodeSnippetViewHolder.UiModel>(binding) {

    data class UiModel(
        @StringRes val textResourceId: Int,
        override val id: String = "codeSnippet_$textResourceId"
    ) : ListItem, InspirationListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = CodeSnippetViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_code_snippet, parent, false)
        )
    }
}