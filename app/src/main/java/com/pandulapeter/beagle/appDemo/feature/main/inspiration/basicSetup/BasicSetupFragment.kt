package com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialContainerTransform
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentBasicSetupBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseViewModelFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.utils.waitForLayout
import com.pandulapeter.beagle.modules.AnimationDurationSwitchModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.android.ext.android.inject

class BasicSetupFragment : BaseViewModelFragment<FragmentBasicSetupBinding, BasicSetupViewModel>(R.layout.fragment_basic_setup, false, R.string.case_study_basic_setup_title) {

    override val viewModel by inject<BasicSetupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            scrimColor = Color.TRANSPARENT
            fadeMode = MaterialContainerTransform.FADE_MODE_OUT
            drawingViewId = R.id.fragment_container
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.root, getString(R.string.case_study_basic_setup_title))
        setupRecyclerView()
        refreshBeagleModules()
        postponeEnterTransition()
    }

    private fun setupRecyclerView() {
        val recyclerAdapter = object : BaseAdapter<ListItem>() {}
        binding.recyclerView.run {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            waitForLayout { startPostponedEnterTransition() }
        }
        viewModel.items.observeForever(recyclerAdapter::submitList)
    }

    private fun refreshBeagleModules() = Beagle.setModules(
        TextModule(text = "Work in progress"),
        AnimationDurationSwitchModule()
    )

    companion object {
        fun newInstance() = BasicSetupFragment()
    }
}