package com.pandulapeter.beagle.views.items.slider

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.R

internal class SliderViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val nameTextView = itemView.findViewById<TextView>(R.id.name)
    private val sliderSeekBar = itemView.findViewById<SeekBar>(R.id.slider)

    @SuppressLint("ClickableViewAccessibility")
    fun bind(viewModel: SliderViewModel) {
        sliderSeekBar.setOnSeekBarChangeListener(null)
        sliderSeekBar.max = viewModel.trick.maximumValue - viewModel.trick.minimumValue
        sliderSeekBar.progress = viewModel.trick.value - viewModel.trick.minimumValue
        nameTextView.text = viewModel.trick.name(viewModel.trick.value - viewModel.trick.minimumValue)
        sliderSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                nameTextView.text = viewModel.trick.name(progress - viewModel.trick.minimumValue)
                viewModel.trick.value = progress - viewModel.trick.minimumValue
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
        sliderSeekBar.setOnTouchListener { v, event ->
            (itemView.parent?.parent as? ViewGroup?)?.requestDisallowInterceptTouchEvent(true)
            false
        }
    }

    companion object {
        fun create(parent: ViewGroup) = SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false))
    }
}