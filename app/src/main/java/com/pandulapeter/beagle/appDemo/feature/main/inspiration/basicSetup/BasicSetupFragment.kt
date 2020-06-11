package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentBasicSetupBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseViewModelFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.modules.TextModule
import org.koin.android.ext.android.inject

class BasicSetupFragment : BaseViewModelFragment<FragmentBasicSetupBinding, BasicSetupViewModel>(R.layout.fragment_basic_setup, false, R.string.case_study_basic_setup_title) {

    override val viewModel by inject<BasicSetupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        refreshBeagleModules()
    }

    private fun setupRecyclerView() {
        val recyclerAdapter = object : BaseAdapter<ListItem>() {}
        binding.recyclerView.run {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        viewModel.items.observeForever(recyclerAdapter::submitList)
    }

    private fun refreshBeagleModules() = Beagle.setModules(
        TextModule(text = "Work in progress")
    )

    companion object {
        fun newInstance() = BasicSetupFragment()
    }
}