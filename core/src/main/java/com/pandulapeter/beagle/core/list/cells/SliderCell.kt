package com.pandulapeter.beagle.core.list.cells

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.contracts.module.ViewHolder
import com.pandulapeter.beagle.core.R

internal data class SliderCell(
    override val id: String,
    private val text: CharSequence,
    private val value: Int,
    private val minimumValue: Int,
    private val maximumValue: Int,
    private val isEnabled: Boolean,
    private val onValueChanged: (Int) -> Unit
) : Cell<SliderCell> {

    override fun createViewHolderDelegate() = object : ViewHolder.Delegate<SliderCell>() {

        override fun createViewHolder(parent: ViewGroup) = SliderViewHolder(parent)
    }

    @SuppressLint("ClickableViewAccessibility")
    private class SliderViewHolder(parent: ViewGroup) : ViewHolder<SliderCell>(LayoutInflater.from(parent.context).inflate(R.layout.beagle_cell_slider, parent, false)) {

        private val textView = itemView.findViewById<TextView>(R.id.beagle_text_view)
        private val seekBar = itemView.findViewById<SeekBar>(R.id.beagle_seek_bar)

        init {
            seekBar.setOnTouchListener { _, _ ->
                (itemView.parent?.parent as? ViewGroup?)?.requestDisallowInterceptTouchEvent(true)
                false
            }
        }

        override fun bind(model: SliderCell) {
            textView.text = model.text
            seekBar.run {
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