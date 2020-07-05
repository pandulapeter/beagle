package com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.NetworkingManager
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list.NetworkRequestInterceptorAdapter
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.networkRequestInterceptor.list.NetworkRequestInterceptorListItem
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.NetworkLogListModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class NetworkRequestInterceptorFragment : InspirationDetailFragment<NetworkRequestInterceptorViewModel, NetworkRequestInterceptorListItem>(R.string.case_study_network_request_interceptor_title) {

    override val viewModel by viewModel<NetworkRequestInterceptorViewModel>()

    override fun createAdapter() = NetworkRequestInterceptorAdapter(
        scope = viewModel.viewModelScope,
        onSongSelected = viewModel::onSongSelected,
        onTryAgainButtonPressed = viewModel::loadSong,
        onClearLogsButtonPressed = ::clearNetworkLogs
    )

    override fun getBeagleModules(): List<Module<*>> = listOf(
        NetworkLogListModule(
            title = getText(R.string.case_study_network_request_interceptor_network_activity),
            isExpandedInitially = true,
            baseUrl = NetworkingManager.BASE_URL
        )
    )

    private fun clearNetworkLogs() {
        Beagle.clearNetworkLogs()
        binding.recyclerView.showSnackbar(R.string.case_study_network_request_interceptor_logs_cleared)
    }

    companion object {
        fun newInstance() = NetworkRequestInterceptorFragment()
    }
}