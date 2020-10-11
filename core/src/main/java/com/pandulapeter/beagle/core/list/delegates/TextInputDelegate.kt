package com.pandulapeter.beagle.core.list.delegates

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.delegates.shared.ValueWrapperModuleDelegate
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.modules.TextInputModule

internal class TextInputDelegate : ValueWrapperModuleDelegate.String<TextInputModule>() {

    override fun createCells(module: TextInputModule): List<Cell<*>> = (DialogInterface.OnClickListener { dialog, button ->
        if (button == DialogInterface.BUTTON_POSITIVE && !module.areRealTimeUpdatesEnabled) {
            setNewValue(module, (dialog as AlertDialog).findViewById<EditText>(R.id.beagle_edit_text)?.text?.toString().orEmpty())
        }
        dialog.dismiss()
    }).let { onDialogButtonPressed ->
        listOf(
            TextCell(
                id = module.id,
                text = if (module.shouldRequireConfirmation && hasPendingChanges(module)) module.text(getUiValue(module)).withSuffix("*") else module.text(getUiValue(module)),
                isEnabled = module.isEnabled,
                isSectionHeader = false,
                icon = null,
                onItemSelected = {
                    BeagleCore.implementation.currentActivity?.applyTheme()?.run {
                        AlertDialog.Builder(this)
                            .setView(R.layout.beagle_view_text_input_dialog)
                            .setPositiveButton(text(module.doneText), onDialogButtonPressed)
                            .apply {
                                if (!module.areRealTimeUpdatesEnabled) {
                                    setNegativeButton(text(module.cancelText), onDialogButtonPressed)
                                }
                            }
                            .create()
                            .apply { setOnShowListener { findViewById<EditText>(R.id.beagle_edit_text)?.initialize(this, module) } }
                            .show()
                    }
                })
        )
    }

    private fun setNewValue(module: TextInputModule, newValue: kotlin.String) {
        if (module.validator(newValue)) {
            setUiValue(module, newValue)
        }
    }

    private fun EditText.initialize(dialog: Dialog, module: TextInputModule) {
        getCurrentValue(module).let { currentValue ->
            setText(currentValue)
            setSelection(currentValue.length)
            requestFocus()
            post { (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, 0) }
            setOnEditorActionListener { _, actionId, _ ->
                true.also {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (!module.areRealTimeUpdatesEnabled) {
                            setNewValue(module, text?.toString().orEmpty())
                        }
                        dialog.dismiss()
                    }
                }
            }
        }
        if (module.areRealTimeUpdatesEnabled) {
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    setNewValue(module, s?.toString().orEmpty())
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            })
        }
    }
}