package com.pandulapeter.beagle.core.view.logDetail

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.createAndShareLogFile
import com.pandulapeter.beagle.utils.mutableLiveDataOf
import kotlinx.coroutines.launch

internal class LogDetailDialogViewModel : ViewModel() {

    private val _isShareButtonEnabled = mutableLiveDataOf(true)
    val isShareButtonEnabled: LiveData<Boolean> = _isShareButtonEnabled

    fun shareLogs(activity: Activity?, content: String, timestamp: Long, id: String, fileName: String?) {
        if (_isShareButtonEnabled.value == true) {
            viewModelScope.launch {
                _isShareButtonEnabled.postValue(false)
                activity?.createAndShareLogFile(
                    fileName = "${fileName ?: BeagleCore.implementation.behavior.logBehavior.getFileName(timestamp, id)}.txt",
                    content = content
                )
                _isShareButtonEnabled.postValue(true)
            }
        }
    }
}