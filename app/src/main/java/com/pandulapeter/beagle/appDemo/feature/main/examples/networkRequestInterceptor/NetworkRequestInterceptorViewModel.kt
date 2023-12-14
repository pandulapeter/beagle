package com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.data.UserRepository
import com.pandulapeter.beagle.appDemo.data.model.User
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.ClearButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.ErrorViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.LoadingIndicatorViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.NetworkRequestInterceptorListItem
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.RadioButtonViewHolder
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.list.UserViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.CodeSnippetViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class NetworkRequestInterceptorViewModel(
    private val userRepository: UserRepository
) : ListViewModel<NetworkRequestInterceptorListItem>() {

    private var selectedEndpoint by Delegates.observable(Endpoints.ENDPOINT_1) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            loadEndpoint()
            refreshItems()
        }
    }
    private var users: List<User>? = null
    private var isLoading: Boolean = false
    private var job: Job? = null
    private val _items = MutableLiveData(listOf<NetworkRequestInterceptorListItem>())
    override val items: LiveData<List<NetworkRequestInterceptorListItem>> = _items

    init {
        loadEndpoint()
        refreshItems()
    }

    fun onRadioButtonSelected(selectedItem: RadioButtonViewHolder.UiModel) {
        Endpoints.fromResourceId(selectedItem.titleResourceId)?.let { selectedEndpoint = it }
    }

    fun loadEndpoint() {
        job?.cancel()
        isLoading = true
        refreshItems()
        job = viewModelScope.launch {
            users = when (selectedEndpoint) {
                Endpoints.ENDPOINT_1 -> userRepository.getAllUsers()
                Endpoints.ENDPOINT_2 -> userRepository.searchForUser("Alison")
            }
            isLoading = false
            refreshItems()
        }
    }

    private fun refreshItems() {
        _items.value = mutableListOf<NetworkRequestInterceptorListItem>().apply {
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_1))
            addAll(Endpoints.entries.map { endpoint -> RadioButtonViewHolder.UiModel(endpoint.titleResourceId, selectedEndpoint == endpoint) })
            if (isLoading) {
                add(LoadingIndicatorViewHolder.UiModel())
            } else {
                if (users.orEmpty().isNotEmpty()) {
                    users.orEmpty().take(5).forEach { user ->
                        add(UserViewHolder.UiModel(user))
                    }
                } else {
                    add(ErrorViewHolder.UiModel())
                }
            }
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_3))
            add(CodeSnippetViewHolder.UiModel("NetworkLogListModule()"))
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_4))
            add(
                CodeSnippetViewHolder.UiModel(
                    "dependencies {\n" +
                            "    …\n" +
                            "    api \"io.github.pandulapeter.beagle:log-okhttp:\$beagleVersion\"\n" +
                            "    \n" +
                            "    // Alternative for Android modules:\n" +
                            "    // debugApi \"io.github.pandulapeter.beagle:log-okhttp:\$beagleVersion\"\n" +
                            "    // releaseApi \"io.github.pandulapeter.beagle:log-okhttp-noop:\$beagleVersion\"\n" +
                            "}"
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_5))
            add(
                CodeSnippetViewHolder.UiModel(
                    "Beagle.initialize(\n" +
                            "    …\n" +
                            "    behavior = Behavior(\n" +
                            "        …\n" +
                            "        networkLogBehavior = Behavior.NetworkLogBehavior(\n" +
                            "            networkLoggers = listOf(BeagleOkHttpLogger),\n" +
                            "            …\n" +
                            "        )\n" +
                            "    )\n" +
                            ")"
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_6))
            add(
                CodeSnippetViewHolder.UiModel(
                    "val client = OkHttpClient.Builder()\n" +
                            "    …\n" +
                            "    .apply { (BeagleOkHttpLogger.logger as? Interceptor?)?.let { addInterceptor(it) } }\n" +
                            "    .build()"
                )
            )
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_7))
            add(ClearButtonViewHolder.UiModel())
            add(TextViewHolder.UiModel(R.string.case_study_network_request_interceptor_text_8))
        }
    }

    private enum class Endpoints(@StringRes val titleResourceId: Int) {
        ENDPOINT_1(titleResourceId = R.string.case_study_network_request_interceptor_endpoint_1),
        ENDPOINT_2(titleResourceId = R.string.case_study_network_request_interceptor_endpoint_2);

        companion object {
            fun fromResourceId(@StringRes titleResourceId: Int?) = entries.firstOrNull { it.titleResourceId == titleResourceId }
        }
    }
}