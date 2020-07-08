package com.pandulapeter.beagle.core.list.delegates

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.contracts.module.Cell
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.list.cells.TextCell
import com.pandulapeter.beagle.core.list.delegates.shared.PersistableModuleDelegate
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.core.util.extension.applyTheme
import com.pandulapeter.beagle.modules.TextInputModule

internal class TextInputDelegate : PersistableModuleDelegate.String<TextInputModule>() {

    override fun createCells(module: TextInputModule): List<Cell<*>> = listOf(
        TextCell(
            id = module.id,
            text = if (module.shouldRequireConfirmation && hasPendingChanges(module)) module.text(getUiValue(module)).append("*") else module.text(getUiValue(module)),
            onItemSelected = {
                BeagleCore.implementation.currentActivity?.applyTheme()?.run {
                    AlertDialog.Builder(this)
                        .setView(R.layout.beagle_view_text_input_dialog)
                        .create()
                        .apply {
                            setOnShowListener {
                                findViewById<EditText>(R.id.beagle_edit_text)?.let { editText ->
                                    getCurrentValue(module).let { currentValue ->
                                        editText.setText(currentValue)
                                        editText.setSelection(currentValue.length)
                                        editText.requestFocus()
                                        editText.setOnEditorActionListener { _, actionId, _ ->
                                            true.also {
                                                if (actionId == EditorInfo.IME_ACTION_DONE) {
                                                    dismiss()
                                                }
                                            }
                                        }
                                    }
                                    editText.addTextChangedListener(object : TextWatcher {
                                        override fun afterTextChanged(s: Editable?) {
                                            s?.toString()?.let { newValue ->
                                                if (module.validator(newValue)) {
                                                    setUiValue(module, newValue)
                                                }
                                            }
                                        }

                                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

                                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
                                    })
                                }
                            }
                        }
                        .show()
                }
            })
    )
}