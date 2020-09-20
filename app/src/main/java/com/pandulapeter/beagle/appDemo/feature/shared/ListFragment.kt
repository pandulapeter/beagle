package com.pandulapeter.beagle.appDemo.feature.shared

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.BR
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentListBinding
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.utils.observe
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.utils.extensions.color
import com.pandulapeter.beagle.utils.extensions.waitForPreDraw

abstract class ListFragment<VM : ListViewModel<LI>, LI : ListItem>(
    @StringRes protected val titleResourceId: Int,
    @ColorRes private val backgroundColorResourceId: Int = R.color.transparent
) : BaseFragment<FragmentListBinding>(R.layout.fragment_list) {

    protected abstract val viewModel: VM

    protected abstract fun getBeagleModules(): List<Module<*>>

    protected abstract fun createAdapter(): BaseAdapter<LI>

    protected open fun createLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(context)

    fun blockGestures() {
        try {
            binding.recyclerView.shouldBlockGestures = { true }
        } catch (_: IllegalStateException) {
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.setVariable(BR.viewModel, viewModel)
        binding.root.setBackgroundColor(requireContext().color(backgroundColorResourceId))
        binding.appBar.setup(titleResourceId, parentFragment?.childFragmentManager?.backStackEntryCount ?: 0 <= 1, requireActivity())
        setupRecyclerView()
        refreshBeagle()
    }

    protected fun refreshBeagle(clearIfEmpty: Boolean = false) = getBeagleModules().let { modules ->
        if (modules.isNotEmpty() || clearIfEmpty) {
            Beagle.set(*modules.toTypedArray())
        }
    }

    private fun setupRecyclerView() {
        val listAdapter = createAdapter().also { it.blockGestures = { binding.recyclerView.shouldBlockGestures = { true } } }
        viewModel.items.observe(owner = viewLifecycleOwner) { listAdapter.submitList(it, ::onListUpdated) }
        binding.recyclerView.run {
            shouldBlockGestures = { true }
            adapter = listAdapter
            layoutManager = createLayoutManager()
            setHasFixedSize(true)
            waitForPreDraw { postDelayed({ startPostponedEnterTransition() }, 100) }
        }
    }

    private fun onListUpdated() {
        try {
            binding.appBar.run {
                postDelayed({
                    try {
                        setLifted(binding.recyclerView.computeVerticalScrollOffset() != 0)
                        binding.recyclerView.shouldBlockGestures = { false }
                    } catch (_: IllegalStateException) {
                    }
                }, 300)
            }
        } catch (_: IllegalStateException) {
        }
    }
}