package com.pandulapeter.beagle.appDemo.feature.main.examples.overlay

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.utils.createTextModule
import com.pandulapeter.beagle.common.configuration.Insets
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.common.listeners.OverlayListener
import com.pandulapeter.beagle.modules.SwitchModule
import com.pandulapeter.beagle.utils.extensions.color
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.min

class OverlayFragment : ExamplesDetailFragment<OverlayViewModel, ListItem>(R.string.case_study_overlay_title), OverlayListener {

    override val viewModel by viewModel<OverlayViewModel>()
    private val paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = requireContext().color(R.color.brand_dark)
            alpha = 192
        }
    }
    private var isSwitchEnabled = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        isSwitchEnabled = savedInstanceState?.getBoolean(IS_SWITCH_ENABLED) ?: isSwitchEnabled
        super.onViewCreated(view, savedInstanceState)
        Beagle.addOverlayListener(
            listener = this,
            lifecycleOwner = viewLifecycleOwner
        )
    }

    override fun createAdapter() = object : BaseAdapter<ListItem>(
        scope = viewModel.viewModelScope
    ) {}

    override fun getBeagleModules(): List<Module<*>> = listOf(
        createTextModule(R.string.case_study_overlay_hint),
        SwitchModule(
            text = getText(R.string.case_study_overlay_enable),
            initialValue = isSwitchEnabled,
            onValueChanged = {
                isSwitchEnabled = it
                Beagle.invalidateOverlay()
            }
        )
    )

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_SWITCH_ENABLED, isSwitchEnabled)
    }

    override fun onDrawOver(canvas: Canvas, insets: Insets) {
        if (isSwitchEnabled) {
            val usableWidth = canvas.width - insets.left - insets.right
            val usableHeight = canvas.height - insets.top - insets.bottom
            canvas.drawCircle(
                insets.left + usableWidth * 0.5f,
                insets.top + usableHeight * 0.5f,
                min(usableWidth, usableHeight) * 0.25f,
                paint
            )
        }
    }

    companion object {
        private const val IS_SWITCH_ENABLED = "isSwitchEnabled"

        fun newInstance() = OverlayFragment()
    }
}