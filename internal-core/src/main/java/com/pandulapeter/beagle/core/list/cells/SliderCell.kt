package com.pandulapeter.beagle.core.list.cells

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.SeekBar
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.databinding.BeagleCellSliderBinding
import com.pandulapeter.beagle.core.util.extension.setText
import com.pandulapeter.beagle.utils.extensions.inflater

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
                setOnSeekBarChangeListener(null)
                max = model.maximumValue - model.minimumValue
                progress = model.value - model.minimumValue
                isEnabled = model.isEnabled
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser && progress != model.value + model.minimumValue) {
                            model.onValueChanged(progress + model.minimumValue)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
                })
            }
        }
    }
}