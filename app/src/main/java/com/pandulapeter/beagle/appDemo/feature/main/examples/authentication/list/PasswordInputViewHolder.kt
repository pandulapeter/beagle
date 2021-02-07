package com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.list

import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.pandulapeter.beagle.appDemo.databinding.ItemAuthenticationPasswordInputBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.inflater

class PasswordInputViewHolder private constructor(
    binding: ItemAuthenticationPasswordInputBinding,
    onDonePressed: () -> Unit
) : BaseViewHolder<ItemAuthenticationPasswordInputBinding, PasswordInputViewHolder.UiModel>(binding) {

    init {
        binding.passwordInput.setOnEditorActionListener { _, actionId, _ ->
            consume {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onDonePressed()
                }
            }
        }
    }

    data class UiModel(
        val initialValue: String
    ) : AuthenticationListItem {

        override val id = "passwordInput"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDonePressed: () -> Unit
        ) = PasswordInputViewHolder(
            binding = ItemAuthenticationPasswordInputBinding.inflate(parent.inflater, parent, false),
            onDonePressed = onDonePressed
        )
    }
}