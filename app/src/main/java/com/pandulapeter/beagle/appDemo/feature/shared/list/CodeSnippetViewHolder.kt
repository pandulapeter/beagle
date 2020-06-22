package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemCodeSnippetBinding
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.list.AuthenticationListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.list.BasicSetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.list.InspirationListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundListItem
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem

class CodeSnippetViewHolder private constructor(
    binding: ItemCodeSnippetBinding
) : BaseViewHolder<ItemCodeSnippetBinding, CodeSnippetViewHolder.UiModel>(binding) {

    data class UiModel(
        val codeSnippet: String,
        override val id: String = "codeSnippet_$codeSnippet"
    ) : ListItem, SetupListItem, InspirationListItem, BasicSetupListItem, AuthenticationListItem, PlaygroundListItem, AboutListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = CodeSnippetViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_code_snippet, parent, false)
        )
    }
}