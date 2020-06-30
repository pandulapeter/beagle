package com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.list

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemFeatureTogglesCurrentStateBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class CurrentStateViewHolder private constructor(
    binding: ItemFeatureTogglesCurrentStateBinding
) : BaseViewHolder<ItemFeatureTogglesCurrentStateBinding, CurrentStateViewHolder.UiModel>(binding) {

    private var characterIndex = 0

    data class UiModel(
        val toggle1: Boolean,
        val toggle2: Boolean,
        val toggle3: Boolean,
        val toggle4: Boolean,
        val multipleSelectionOptions: List<String>,
        val singleSelectionOption: String?
    ) : FeatureTogglesListItem {

        override val id = "currentState"
    }

    override fun bind(uiModel: UiModel) {
        super.bind(uiModel)
        if (uiModel.singleSelectionOption != null) {
            binding.textView.run {
                val on = context.getString(R.string.case_study_feature_toggles_current_state_on)
                val off = context.getString(R.string.case_study_feature_toggles_current_state_off)
                val title = context.getString(R.string.case_study_feature_toggles_current_state_title)
                val key1 = context.getString(R.string.case_study_feature_toggles_current_state_key_1)
                val key2 = context.getString(R.string.case_study_feature_toggles_current_state_key_2)
                val key3 = context.getString(R.string.case_study_feature_toggles_current_state_key_3)
                val key4 = context.getString(R.string.case_study_feature_toggles_current_state_key_4)
                val key5 = context.getString(R.string.case_study_feature_toggles_current_state_key_5)
                val key6 = context.getString(R.string.case_study_feature_toggles_current_state_key_6)
                val value1 = if (uiModel.toggle1) on else off
                val value2 = if (uiModel.toggle2) on else off
                val value3 = if (uiModel.toggle3) on else off
                val value4 = if (uiModel.toggle4) on else off
                val value5 = if (uiModel.multipleSelectionOptions.isEmpty()) "-" else uiModel.multipleSelectionOptions.sorted().joinToString()
                val value6 = uiModel.singleSelectionOption ?: "-"
                text = SpannableString(title + key1 + value1 + key2 + value2 + key3 + value3 + key4 + value4 + key5 + value5 + key6 + value6).apply {
                    characterIndex = 0
                    setBold(0, title.length)
                    setBold(key1.length, value1.length)
                    setBold(key2.length, value2.length)
                    setBold(key3.length, value3.length)
                    setBold(key4.length, value4.length)
                    setBold(key5.length, value5.length)
                    setBold(key6.length, value6.length)
                }
            }
        }
    }

    private fun SpannableString.setBold(startIndexIncrement: Int, length: Int) {
        setSpan(StyleSpan(Typeface.BOLD), characterIndex + startIndexIncrement, characterIndex + startIndexIncrement + length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        characterIndex += startIndexIncrement + length
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = CurrentStateViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_feature_toggles_current_state, parent, false)
        )
    }
}