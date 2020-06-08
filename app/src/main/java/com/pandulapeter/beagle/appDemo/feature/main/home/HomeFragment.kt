package com.pandulapeter.beagle.appDemo.feature.main.home

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentHomeBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home, true, R.string.home_title) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeAdapter = HomeAdapter()
        binding.recyclerView.run {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}