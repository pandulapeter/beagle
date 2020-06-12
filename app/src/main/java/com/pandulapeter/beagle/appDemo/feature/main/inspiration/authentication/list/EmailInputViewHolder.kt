package com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemAuthenticationEmailInputBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class EmailInputViewHolder private constructor(
    binding: ItemAuthenticationEmailInputBinding
) : BaseViewHolder<ItemAuthenticationEmailInputBinding, EmailInputViewHolder.UiModel>(binding) {

    data class UiModel(
        val initialValue: String
    ) : AuthenticationListItem {

        override val id = "emailInput"
    }

    companion object {
        fun create(
            parent: ViewGroup
        ) = EmailInputViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_authentication_email_input, parent, false)
        )
    }
}