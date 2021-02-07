package com.pandulapeter.beagle.appDemo.feature.main.setup.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemSetupGithubButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.utils.extensions.drawable
import com.pandulapeter.beagle.utils.extensions.inflater

class GithubButtonViewHolder private constructor(
    binding: ItemSetupGithubButtonBinding,
    onButtonPressed: () -> Unit
) : BaseViewHolder<ItemSetupGithubButtonBinding, GithubButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onButtonPressed()
            }
        }
        binding.button.setCompoundDrawablesWithIntrinsicBounds(null, null, binding.button.context.drawable(R.drawable.ic_github), null)
    }

    data class UiModel(
        override val id: String = "button"
    ) : SetupListItem

    companion object {
        fun create(
            parent: ViewGroup,
            onButtonPressed: () -> Unit
        ) = GithubButtonViewHolder(
            binding = ItemSetupGithubButtonBinding.inflate(parent.inflater, parent, false),
            onButtonPressed = onButtonPressed
        )
    }
}