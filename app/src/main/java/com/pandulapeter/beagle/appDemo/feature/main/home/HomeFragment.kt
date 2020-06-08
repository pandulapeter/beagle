package com.pandulapeter.beagle.appDemo.feature.main.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentHomeBinding
import com.pandulapeter.beagle.appDemo.feature.main.home.list.HomeAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.BaseViewModelFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseViewModelFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home, true, R.string.home_title) {

    override val viewModel by viewModel<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeAdapter = HomeAdapter()
        binding.recyclerView.run {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
        viewModel.items.observeForever(homeAdapter::submitList)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}