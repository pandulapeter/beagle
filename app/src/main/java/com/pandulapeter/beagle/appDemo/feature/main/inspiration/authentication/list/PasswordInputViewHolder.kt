package com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemAuthenticationPasswordInputBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class PasswordInputViewHolder private constructor(
    binding: ItemAuthenticationPasswordInputBinding
) : BaseViewHolder<ItemAuthenticationPasswordInputBinding, PasswordInputViewHolder.UiModel>(binding) {

    data class UiModel(
        val initialValue: String
    ) : AuthenticationListItem {

        override val id = "passwordInput"
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = PasswordInputViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_authentication_password_input, parent, false)
        )
    }
}