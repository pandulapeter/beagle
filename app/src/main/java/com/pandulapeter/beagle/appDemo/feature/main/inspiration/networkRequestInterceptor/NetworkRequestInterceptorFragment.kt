package com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.NetworkLogListModule
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class NetworkRequestInterceptorFragment : InspirationDetailFragment<NetworkRequestInterceptorViewModel, ListItem>(R.string.case_study_network_request_interceptor_title) {

    override val viewModel by viewModel<NetworkRequestInterceptorViewModel>()

    override fun createAdapter() = object : BaseAdapter<ListItem>(viewModel.viewModelScope) {}

    override fun getBeagleModules(): List<Module<*>> = listOf(
        NetworkLogListModule()
    )

    companion object {
        fun newInstance() = NetworkRequestInterceptorFragment()
    }
}