package com.pandulapeter.beagle.appDemo.feature.main.inspiration

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentInspirationDetailBinding
import com.pandulapeter.beagle.appDemo.feature.shared.ViewModelFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.utils.color
import com.pandulapeter.beagle.appDemo.utils.waitForLayout

abstract class InspirationDetailFragment<VM : InspirationDetailViewModel<LI>, LI : ListItem>(
    @StringRes titleResourceId: Int
) : ViewModelFragment<FragmentInspirationDetailBinding, VM>(R.layout.fragment_inspiration_detail, titleResourceId, false) {

    override val appBar get() = binding.appBar
    override val backgroundColor get() = requireContext().color(R.color.white)

    abstract fun createAdapter(): BaseAdapter<LI>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
            fadeMode = MaterialContainerTransform.FADE_MODE_OUT
            drawingViewId = R.id.main_fragment_container
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        ViewCompat.setTransitionName(binding.root, getString(titleResourceId))
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val listAdapter = createAdapter()
        binding.recyclerView.run {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            waitForLayout { startPostponedEnterTransition() }
        }
        viewModel.items.observeForever(listAdapter::submitList)
    }
}