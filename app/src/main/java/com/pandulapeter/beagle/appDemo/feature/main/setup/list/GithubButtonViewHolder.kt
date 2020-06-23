package com.pandulapeter.beagle.appDemo.feature.main.setup.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.ItemSetupGithubButtonBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseViewHolder
import com.pandulapeter.beagle.appDemo.utils.drawable

class GithubButtonViewHolder private constructor(
    binding: ItemSetupGithubButtonBinding,
    onButtonClicked: () -> Unit
) : BaseViewHolder<ItemSetupGithubButtonBinding, GithubButtonViewHolder.UiModel>(binding) {

    init {
        binding.root.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                onButtonClicked()
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
            onButtonClicked: () -> Unit
        ) = GithubButtonViewHolder(
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_setup_github_button, parent, false),
            onButtonClicked = onButtonClicked
        )
    }
}