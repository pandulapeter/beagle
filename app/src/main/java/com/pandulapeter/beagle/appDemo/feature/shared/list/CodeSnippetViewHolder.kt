package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemCodeSnippetBinding
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list.AnalyticsListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.list.AuthenticationListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureFlags.list.FeatureFlagsListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.list.ExamplesListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.list.MockDataGeneratorListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.NetworkRequestInterceptorListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.SimpleSetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundListItem
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem

class CodeSnippetViewHolder private constructor(
    binding: ItemCodeSnippetBinding
) : BaseViewHolder<ItemCodeSnippetBinding, CodeSnippetViewHolder.UiModel>(binding) {

    data class UiModel(
        val codeSnippet: String,
        override val id: String = "codeSnippet_$codeSnippet"
    ) : ListItem, SetupListItem, ExamplesListItem, SimpleSetupListItem, FeatureFlagsListItem, AuthenticationListItem, NetworkRequestInterceptorListItem, AnalyticsListItem,
        MockDataGeneratorListItem, PlaygroundListItem, AboutListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = CodeSnippetViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_code_snippet, parent, false)
        )
    }
}