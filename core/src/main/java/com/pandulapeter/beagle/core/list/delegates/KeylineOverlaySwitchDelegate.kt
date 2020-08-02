package com.pandulapeter.beagle.core.list.delegates

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Insets
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
            override fun onDrawOver(canvas: Canvas, insets: Insets) {
                if (module == null) {
                    BeagleCore.implementation.currentActivity?.let { tryToInitialize(it) }
                }
                module?.let {
                    val processedInsets = it.applyInsets?.invoke(insets) ?: insets
                    canvas.drawGridIfNeeded(it, processedInsets.left, processedInsets.top, processedInsets.right, processedInsets.bottom)
                }
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
            gridPaint.strokeWidth = context.dimension(R.dimen.beagle_divider).toFloat()
            keylinePaint.strokeWidth = context.dimension(R.dimen.beagle_divider).toFloat()
            keylineGrid = module.grid ?: context.dimension(R.dimen.beagle_default_keyline_grid)
            keylinePrimary = (module.keylinePrimary ?: context.dimension(R.dimen.beagle_default_keyline_primary)).toFloat()
            keylineSecondary = (module.keylineSecondary ?: context.dimension(R.dimen.beagle_default_keyline_secondary)).toFloat()
        }
    }

    //TODO: Does not work well for RTL layouts.
    private fun Canvas.drawGridIfNeeded(module: KeylineOverlaySwitchModule, leftInset: Int, topInset: Int, rightInset: Int, bottomInset: Int) {
        if (module.getCurrentValue(BeagleCore.implementation) == true) {
            gridPaint.color = module.color ?: BeagleCore.implementation.currentActivity?.colorResource(android.R.attr.textColorPrimary) ?: gridPaint.color
            keylinePaint.color = gridPaint.color
            gridPaint.alpha = GRID_ALPHA
            keylinePaint.alpha = KEYLINE_ALPHA
            for (x in 0..width step keylineGrid) {
                drawLine(leftInset + x.toFloat(), topInset.toFloat(), leftInset + x.toFloat(), height.toFloat() - bottomInset, gridPaint)
            }
            for (y in 0..height step keylineGrid) {
                drawLine(leftInset.toFloat(), topInset + y.toFloat(), width.toFloat() - rightInset, topInset + y.toFloat(), gridPaint)
            }
            drawLine(leftInset + keylinePrimary, 0f, leftInset + keylinePrimary, height.toFloat(), keylinePaint)
            drawLine(leftInset + keylineSecondary, 0f, leftInset + keylineSecondary, height.toFloat(), keylinePaint)
            drawLine(width - keylinePrimary - rightInset, 0f, width - keylinePrimary - rightInset, height.toFloat(), keylinePaint)
        }
    }

    companion object {
        private const val GRID_ALPHA = 32
        private const val KEYLINE_ALPHA = 64
    }
}