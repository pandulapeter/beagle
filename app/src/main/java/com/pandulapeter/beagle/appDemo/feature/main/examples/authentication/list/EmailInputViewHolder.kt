package com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.databinding.ItemAuthenticationEmailInputBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

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
            binding = ItemAuthenticationEmailInputBinding.inflate(parent.inflater, parent, false)
        )
    }
}