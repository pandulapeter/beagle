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
import com.pandulapeter.beagle.appDemo.feature.shared.DestinationFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.utils.shouldUseContainerTransform
import com.pandulapeter.beagle.appDemo.utils.waitForLayout

abstract class InspirationDetailFragment<VM : InspirationDetailViewModel<LI>, LI : ListItem>(
    @StringRes titleResourceId: Int
) : DestinationFragment<FragmentInspirationDetailBinding, VM>(R.layout.fragment_inspiration_detail, titleResourceId, R.color.window_background) {

    override val appBar get() = binding.appBar

    abstract fun createAdapter(): BaseAdapter<LI>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (shouldUseContainerTransform) {
            sharedElementEnterTransition = MaterialContainerTransform().apply {
                scrimColor = Color.TRANSPARENT
                fadeProgressThresholds = MaterialContainerTransform.ProgressThresholds(0f, 0.5f)
                scaleProgressThresholds = MaterialContainerTransform.ProgressThresholds(0f, 1f)
                scaleMaskProgressThresholds = MaterialContainerTransform.ProgressThresholds(0f, 1f)
                shapeMaskProgressThresholds = MaterialContainerTransform.ProgressThresholds(0f, 1f)
                fadeMode = MaterialContainerTransform.FADE_MODE_IN
                drawingViewId = R.id.fragment_container
            }
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