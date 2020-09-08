package com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.list

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemValueWrappersCurrentStateBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class CurrentStateViewHolder private constructor(
    binding: ItemValueWrappersCurrentStateBinding,
    onCurrentStateCardPressed: () -> Unit
) : BaseViewHolder<ItemValueWrappersCurrentStateBinding, CurrentStateViewHolder.UiModel>(binding) {

    private var characterIndex = 0

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onCurrentStateCardPressed()
            }
        }
    }

    data class UiModel(
        val toggle1: Boolean,
        val toggle2: Boolean,
        val toggle3: Boolean,
        val toggle4: Boolean,
        val multipleSelectionOptions: List<String>,
        val singleSelectionOption: String?,
        val slider: Int,
        val text: String
    ) : ValueWrappersListItem {

        override val id = "currentState"
    }

    override fun bind(uiModel: UiModel) {
        super.bind(uiModel)
        binding.textView.run {
            if (uiModel.singleSelectionOption != null || text.isBlank()) {
                val on = context.getString(R.string.case_study_value_wrappers_current_state_on)
                val off = context.getString(R.string.case_study_value_wrappers_current_state_off)
                val key1 = context.getString(R.string.case_study_value_wrappers_current_state_key_1)
                val key2 = context.getString(R.string.case_study_value_wrappers_current_state_key_2)
                val key3 = context.getString(R.string.case_study_value_wrappers_current_state_key_3)
                val key4 = context.getString(R.string.case_study_value_wrappers_current_state_key_4)
                val key5 = context.getString(R.string.case_study_value_wrappers_current_state_key_5)
                val key6 = context.getString(R.string.case_study_value_wrappers_current_state_key_6)
                val key7 = context.getString(R.string.case_study_value_wrappers_current_state_key_7)
                val key8 = context.getString(R.string.case_study_value_wrappers_current_state_key_8)
                val value1 = if (uiModel.toggle1) on else off
                val value2 = if (uiModel.toggle2) on else off
                val value3 = if (uiModel.toggle3) on else off
                val value4 = if (uiModel.toggle4) on else off
                val value5 = if (uiModel.multipleSelectionOptions.isEmpty()) "-" else uiModel.multipleSelectionOptions.sorted().joinToString()
                val value6 = uiModel.singleSelectionOption.orEmpty()
                val value7 = uiModel.slider.toString()
                val value8 = uiModel.text
                text = SpannableString(key1 + value1 + key2 + value2 + key3 + value3 + key4 + value4 + key5 + value5 + key6 + value6 + key7 + value7 + key8 + value8).apply {
                    characterIndex = 0
                    setBold(key1.length, value1.length)
                    setBold(key2.length, value2.length)
                    setBold(key3.length, value3.length)
                    setBold(key4.length, value4.length)
                    setBold(key5.length, value5.length)
                    setBold(key6.length, value6.length)
                    setBold(key7.length, value7.length)
                    setBold(key8.length, value8.length)
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
            parent: ViewGroup,
            onCurrentStateCardPressed: () -> Unit
        ) = CurrentStateViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_value_wrappers_current_state, parent, false),
            onCurrentStateCardPressed = onCurrentStateCardPressed
        )
    }
}