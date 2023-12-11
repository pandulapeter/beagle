package com.pandulapeter.beagle.appDemo.feature.main.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutListItem
import com.pandulapeter.beagle.appDemo.feature.main.about.list.ClickableItemViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class AboutViewModel : ListViewModel<AboutListItem>() {

    var snackbarMessage = MutableLiveData<Int?>()

    override val items: LiveData<List<AboutListItem>> = MutableLiveData(
        mutableListOf<AboutListItem>().apply {
            add(TextViewHolder.UiModel(R.string.about_description))
            add(ClickableItemViewHolder.UiModel(R.string.about_github, R.drawable.ic_github))
            add(ClickableItemViewHolder.UiModel(R.string.about_article, R.drawable.ic_article))
            add(ClickableItemViewHolder.UiModel(R.string.about_google_play, R.drawable.ic_google_play))
            add(ClickableItemViewHolder.UiModel(R.string.about_share, R.drawable.ic_share))
            add(ClickableItemViewHolder.UiModel(R.string.about_contact, R.drawable.ic_contact))
            add(ClickableItemViewHolder.UiModel(R.string.about_open_source, R.drawable.ic_open_source))
        }
    )
}