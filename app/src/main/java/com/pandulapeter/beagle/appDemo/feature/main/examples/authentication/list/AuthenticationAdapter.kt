package com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import kotlinx.coroutines.CoroutineScope

class AuthenticationAdapter(
    scope: CoroutineScope,
    private val onButtonPressed: () -> Unit
) : BaseAdapter<AuthenticationListItem>(scope) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is EmailInputViewHolder.UiModel -> R.layout.item_authentication_email_input
        is PasswordInputViewHolder.UiModel -> R.layout.item_authentication_password_input
        is ButtonViewHolder.UiModel -> R.layout.item_authentication_button
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_authentication_email_input -> EmailInputViewHolder.create(parent)
        R.layout.item_authentication_password_input -> PasswordInputViewHolder.create(parent, onButtonPressed)
        R.layout.item_authentication_button -> ButtonViewHolder.create(parent, onButtonPressed)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is EmailInputViewHolder -> holder.bind(getItem(position) as EmailInputViewHolder.UiModel)
        is PasswordInputViewHolder -> holder.bind(getItem(position) as PasswordInputViewHolder.UiModel)
        is ButtonViewHolder -> holder.bind(getItem(position) as ButtonViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}