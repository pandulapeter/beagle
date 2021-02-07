package com.pandulapeter.beagle.core.list.cells

import android.annotation.SuppressLint
import android.view.ViewGroup
import com.google.android.material.slider.Slider
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.databinding.BeagleCellSliderBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.inflater
import kotlin.math.roundToInt

internal data class SliderCell(
    override val id: String,
    private val text: Text,
    private val value: Int,
    private val minimumValue: Int,
    private val maximumValue: Int,
    private val isEnabled: Boolean,
    private val onValueChanged: (Int) -> Unit
) : Cell<SliderCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<SliderCell>() {

        override fun createViewHolder(parent: ViewGroup) = SliderViewHolder(
            binding = BeagleCellSliderBinding.inflate(parent.inflater, parent, false)
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private class SliderViewHolder(
        private val binding: BeagleCellSliderBinding
    ) : ViewHolder<SliderCell>(binding.root) {

        init {
            binding.beagleSeekBar.setOnTouchListener { _, _ ->
                (itemView.parent?.parent as? ViewGroup?)?.requestDisallowInterceptTouchEvent(true)
                false
            }
        }

        override fun bind(model: SliderCell) {
            binding.beagleTextView.setText(model.text)
            binding.beagleSeekBar.run {
                clearOnChangeListeners()
                valueTo = (model.maximumValue - model.minimumValue).toFloat()
                value = (model.value - model.minimumValue).toFloat()
                stepSize = 1f
                isEnabled = model.isEnabled
                addOnChangeListener(Slider.OnChangeListener { _, value, fromUser ->
                    if (fromUser && value.roundToInt() != model.value + model.minimumValue) {
                        model.onValueChanged((value + model.minimumValue).roundToInt())
                    }
                })
            }
        }
    }
}