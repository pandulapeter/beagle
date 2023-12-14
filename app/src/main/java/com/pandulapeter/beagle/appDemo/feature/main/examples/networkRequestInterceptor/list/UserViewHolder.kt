package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.data.model.User
import com.pandulapeter.beagle.appDemo.databinding.ItemNetworkRequestInterceptorUserBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.inflater

class UserViewHolder private constructor(
    binding: ItemNetworkRequestInterceptorUserBinding,
    onSongCardPressed: () -> Unit
) : BaseViewHolder<ItemNetworkRequestInterceptorUserBinding, UserViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onSongCardPressed()
            }
        }
    }

    data class UiModel(
        private val user: User
    ) : NetworkRequestInterceptorListItem {

        override val id = "user_${user.id}"
        val name = "${user.firstName} ${user.lastName}"
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onSongCardPressed: () -> Unit
        ) = UserViewHolder(
            binding = ItemNetworkRequestInterceptorUserBinding.inflate(parent.inflater, parent, false),
            onSongCardPressed = onSongCardPressed
        )
    }
}