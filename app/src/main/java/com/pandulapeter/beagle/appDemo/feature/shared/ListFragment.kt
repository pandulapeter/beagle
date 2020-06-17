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
import com.pandulapeter.beagle.appDemo.utils.color
import com.pandulapeter.beagle.appDemo.utils.observe
import com.pandulapeter.beagle.appDemo.utils.waitForLayout
import com.pandulapeter.beagle.common.contracts.module.Module

abstract class ListFragment<VM : ListViewModel<LI>, LI : ListItem>(
    @StringRes protected val titleResourceId: Int,
    @ColorRes private val backgroundColorResourceId: Int = R.color.transparent
) : BaseFragment<FragmentListBinding>(R.layout.fragment_list) {

    protected abstract val viewModel: VM

    protected abstract fun getBeagleModules(): List<Module<*>>

    protected abstract fun createAdapter(): BaseAdapter<LI>

    protected open fun createLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(context)

    protected open fun onListUpdated() = Unit

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        binding.root.setBackgroundColor(requireContext().color(backgroundColorResourceId))
        binding.appBar.setup(titleResourceId, parentFragmentManager.backStackEntryCount <= 1, requireActivity())
        setupRecyclerView()
        refreshBeagle()
    }

    protected fun refreshBeagle() = Beagle.setModules(*getBeagleModules().toTypedArray())

    private fun setupRecyclerView() {
        val listAdapter = createAdapter()
        binding.recyclerView.run {
            adapter = listAdapter
            layoutManager = createLayoutManager()
            setHasFixedSize(true)
            waitForLayout { startPostponedEnterTransition() }
        }
        viewModel.items.observe(viewLifecycleOwner) { listAdapter.submitList(it, ::onListUpdated) }
    }
}