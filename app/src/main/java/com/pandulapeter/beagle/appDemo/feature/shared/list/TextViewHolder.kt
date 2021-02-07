package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.view.ViewGroup
import androidx.annotation.StringRes
import com.pandulapeter.beagle.appDemo.databinding.ItemTextBinding
import com.pandulapeter.beagle.appDemo.feature.main.about.licences.list.LicencesListItem
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list.AnalyticsListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.list.AuthenticationListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.list.ExamplesListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.list.MockDataGeneratorListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.NetworkRequestInterceptorListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.list.SimpleSetupListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list.ValueWrappersListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.list.AddModuleListItem
import com.pandulapeter.beagle.appDemo.feature.main.playground.list.PlaygroundListItem
import com.pandulapeter.beagle.appDemo.feature.main.setup.list.SetupListItem
import com.pandulapeter.beagle.utils.extensions.inflater

class TextViewHolder private constructor(
    binding: ItemTextBinding
) : BaseViewHolder<ItemTextBinding, TextViewHolder.UiModel>(binding) {

    data class UiModel(
        @StringRes val textResourceId: Int,
        override val id: String = "text_$textResourceId"
    ) : ListItem, SetupListItem, ExamplesListItem, SimpleSetupListItem, ValueWrappersListItem, AuthenticationListItem, NetworkRequestInterceptorListItem, AnalyticsListItem,
        MockDataGeneratorListItem,
        PlaygroundListItem, AboutListItem, AddModuleListItem, LicencesListItem

    companion object {
        fun create(
            parent: ViewGroup
        ) = TextViewHolder(
            binding = ItemTextBinding.inflate(parent.inflater, parent, false)
        )
    }
}