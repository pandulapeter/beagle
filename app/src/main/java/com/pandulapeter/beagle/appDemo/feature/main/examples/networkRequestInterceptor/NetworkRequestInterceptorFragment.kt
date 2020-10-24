package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.networking.Constants
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.NetworkRequestInterceptorAdapter
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.NetworkRequestInterceptorListItem
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.NetworkLogListModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class NetworkRequestInterceptorFragment :
    ExamplesDetailFragment<NetworkRequestInterceptorViewModel, NetworkRequestInterceptorListItem>(R.string.case_study_network_request_interceptor_title) {

    override val viewModel by viewModel<NetworkRequestInterceptorViewModel>()

    override fun createAdapter() = NetworkRequestInterceptorAdapter(
        scope = viewModel.viewModelScope,
        onSongCardPressed = { Beagle.show() },
        onSongSelected = viewModel::onRadioButtonSelected,
        onTryAgainButtonPressed = viewModel::loadSong,
        onClearLogsButtonPressed = ::clearNetworkLogs
    )

    override fun getBeagleModules(): List<Module<*>> = listOf(
        NetworkLogListModule(
            title = R.string.case_study_network_request_interceptor_network_activity.toText(),
            isExpandedInitially = true,
            baseUrl = Constants.BASE_URL
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