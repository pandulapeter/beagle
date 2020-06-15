package com.pandulapeter.beagle.appDemo.feature.shared.list

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentListBinding
import com.pandulapeter.beagle.appDemo.feature.shared.DestinationFragment
import com.pandulapeter.beagle.appDemo.utils.waitForLayout

abstract class ListFragment<VM : ListViewModel<LI>, LI : ListItem>(
    @StringRes titleResourceId: Int,
    @ColorRes backgroundColorResourceId: Int = R.color.transparent
) : DestinationFragment<FragmentListBinding, VM>(R.layout.fragment_list, titleResourceId, backgroundColorResourceId) {

    final override val appBar get() = binding.appBar

    abstract fun createAdapter(): BaseAdapter<LI>

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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