package com.pandulapeter.beagle.appDemo.feature.main.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutListItem
import com.pandulapeter.beagle.appDemo.feature.main.about.list.ClickableItemViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class AboutViewModel : ListViewModel<AboutListItem>() {

    override val items: LiveData<List<AboutListItem>> = MutableLiveData(
        listOf(
            TextViewHolder.UiModel(R.string.about_description),
            ClickableItemViewHolder.UiModel(R.string.about_github_repository, R.drawable.ic_github)
        )
    )
}