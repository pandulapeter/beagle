package com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.list

import android.view.ViewGroup
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder

class AuthenticationAdapter : BaseAdapter<AuthenticationListItem>() {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is EmailInputViewHolder.UiModel -> R.layout.item_authentication_email_input
        is PasswordInputViewHolder.UiModel -> R.layout.item_authentication_password_input
        else -> super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*, *> = when (viewType) {
        R.layout.item_authentication_email_input -> EmailInputViewHolder.create(parent)
        R.layout.item_authentication_password_input -> PasswordInputViewHolder.create(parent)
        else -> super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*, *>, position: Int) = when (holder) {
        is EmailInputViewHolder -> holder.bind(getItem(position) as EmailInputViewHolder.UiModel)
        is PasswordInputViewHolder -> holder.bind(getItem(position) as PasswordInputViewHolder.UiModel)
        else -> super.onBindViewHolder(holder, position)
    }
}