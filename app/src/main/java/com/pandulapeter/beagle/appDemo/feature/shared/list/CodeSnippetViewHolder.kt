package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.databinding.ItemCodeSnippetBinding
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list.AnalyticsListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.list.AuthenticationListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.list.ExamplesListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.list.MockDataGeneratorListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.NetworkRequestInterceptorListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.SimpleSetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list.ValueWrappersListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundListItem
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.utils.extensions.inflater

class CodeSnippetViewHolder private constructor(
    binding: ItemCodeSnippetBinding
) : BaseViewHolder<ItemCodeSnippetBinding, CodeSnippetViewHolder.UiModel>(binding) {

    data class UiModel(
        val codeSnippet: String,
        override val id: String = "codeSnippet_$codeSnippet"
    ) : ListItem, SetupListItem, ExamplesListItem, SimpleSetupListItem, ValueWrappersListItem, AuthenticationListItem, NetworkRequestInterceptorListItem, AnalyticsListItem,
        MockDataGeneratorListItem, PlaygroundListItem, AboutListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = CodeSnippetViewHolder(
            binding = ItemCodeSnippetBinding.inflate(parent.inflater, parent, false),
        )
    }
}