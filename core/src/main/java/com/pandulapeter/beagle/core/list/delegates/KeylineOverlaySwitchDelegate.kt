package com.pandulapeter.beagle.core.list.delegates

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.common.listeners.OverlayListener
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.list.cells.SwitchCell
import com.pandulapeter.beagle.core.list.delegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.core.util.extension.colorResource
import com.pandulapeter.beagle.core.util.extension.dimension
import com.pandulapeter.beagle.modules.KeylineOverlaySwitchModule

internal class KeylineOverlaySwitchDelegate : PersistableModuleDelegate.Boolean<KeylineOverlaySwitchModule>() {

    private var module: KeylineOverlaySwitchModule? = null
    private val gridPaint = Paint()
    private val keylinePaint = Paint()
    private var keylineGrid = 0
    private var keylinePrimary = 0f
    private var keylineSecondary = 0f

    init {
        BeagleCore.implementation.addInternalOverlayListener(object : OverlayListener {
            override fun onDrawOver(canvas: Canvas) {
                if (module == null) {
                    BeagleCore.implementation.currentActivity?.let { tryToInitialize(it) }
                }
                module?.let { canvas.drawGridIfNeeded(it) }
            }
        })
    }

    override fun createCells(module: KeylineOverlaySwitchModule): List<Cell<*>> = listOf(
        SwitchCell(
            id = module.id,
            text = module.text,
            isChecked = getCurrentValue(module),
            onValueChanged = { newValue -> setCurrentValue(module, newValue) })
    )

    override fun callOnValueChanged(module: KeylineOverlaySwitchModule, newValue: kotlin.Boolean) {
        BeagleCore.implementation.invalidateOverlay()
        super.callOnValueChanged(module, newValue)
    }

    private fun tryToInitialize(context: Context) {
        BeagleCore.implementation.find<KeylineOverlaySwitchModule>(KeylineOverlaySwitchModule.ID)?.let { module ->
            this.module = module
            gridPaint.color = module.color ?: context.colorResource(android.R.attr.textColorPrimary)
            gridPaint.alpha = GRID_ALPHA
            gridPaint.strokeWidth = context.dimension(R.dimen.beagle_divider).toFloat()
            keylinePaint.color = module.color ?: context.colorResource(android.R.attr.textColorPrimary)
            keylinePaint.alpha = KEYLINE_ALPHA
            keylinePaint.strokeWidth = context.dimension(R.dimen.beagle_divider).toFloat()
            keylineGrid = module.grid ?: context.dimension(R.dimen.beagle_default_keyline_grid)
            keylinePrimary = (module.keylinePrimary ?: context.dimension(R.dimen.beagle_default_keyline_primary)).toFloat()
            keylineSecondary = (module.keylineSecondary ?: context.dimension(R.dimen.beagle_default_keyline_secondary)).toFloat()
        }
    }

    //TODO: Does not work well for RTL layouts.
    private fun Canvas.drawGridIfNeeded(module: KeylineOverlaySwitchModule) {
        if (module.getCurrentValue(BeagleCore.implementation) == true) {
            for (x in 0..width step keylineGrid) {
                drawLine(x.toFloat(), 0f, x.toFloat(), height.toFloat(), gridPaint)
            }
            for (y in 0..height step keylineGrid) {
                drawLine(0f, y.toFloat(), width.toFloat(), y.toFloat(), gridPaint)
            }
            drawLine(keylinePrimary, 0f, keylinePrimary, height.toFloat(), keylinePaint)
            drawLine(keylineSecondary, 0f, keylineSecondary, height.toFloat(), keylinePaint)
            drawLine(width - keylinePrimary, 0f, width - keylinePrimary, height.toFloat(), keylinePaint)
        }
    }

    companion object {
        private const val GRID_ALPHA = 32
        private const val KEYLINE_ALPHA = 64
    }
}