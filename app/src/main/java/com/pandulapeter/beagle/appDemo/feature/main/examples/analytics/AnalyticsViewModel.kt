package com.pandulapeter.beagle.appDemo.feature.main.examples.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list.AnalyticsListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list.ClearButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.list.SwitchViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AnalyticsViewModel : ListViewModel<AnalyticsListItem>() {

    private val _items = MutableLiveData(listOf<AnalyticsListItem>())
    override val items: LiveData<List<AnalyticsListItem>> = _items
    private var toggle1 by ToggleDelegate(1)
    private var toggle2 by ToggleDelegate(2)
    private var toggle3 by ToggleDelegate(3)
    private var toggle4 by ToggleDelegate(4)

    init {
        refreshItems()
    }

    fun onSwitchToggled(textResourceId: Int, value: Boolean) {
        when (textResourceId) {
            R.string.case_study_analytics_toggle_1 -> toggle1 = value
            R.string.case_study_analytics_toggle_2 -> toggle2 = value
            R.string.case_study_analytics_toggle_3 -> toggle3 = value
            R.string.case_study_analytics_toggle_4 -> toggle4 = value
        }
    }

    private fun refreshItems() {
        _items.value = listOf(
            TextViewHolder.UiModel(R.string.case_study_analytics_text_1),
            SwitchViewHolder.UiModel(R.string.case_study_analytics_toggle_1, toggle1),
            SwitchViewHolder.UiModel(R.string.case_study_analytics_toggle_2, toggle2),
            SwitchViewHolder.UiModel(R.string.case_study_analytics_toggle_3, toggle3),
            SwitchViewHolder.UiModel(R.string.case_study_analytics_toggle_4, toggle4),
            TextViewHolder.UiModel(R.string.case_study_analytics_text_2),
            ClearButtonViewHolder.UiModel()
        )
    }

    private class ToggleDelegate(private val toggleIndex: Int) : ReadWriteProperty<AnalyticsViewModel, Boolean> {
        private var currentValue = false

        override fun getValue(thisRef: AnalyticsViewModel, property: KProperty<*>) = currentValue

        override fun setValue(thisRef: AnalyticsViewModel, property: KProperty<*>, value: Boolean) {
            if (value != currentValue) {
                currentValue = value
                Beagle.log(tag = AnalyticsFragment.LOG_TAG, message = "Toggle $toggleIndex turned ${if (value) "on" else "off"}.")
                thisRef.refreshItems()
            }
        }

    }
}