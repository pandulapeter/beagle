package com.pandulapeter.beagle.core.view.logDetail

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.util.extension.createAndShareLogFile
import kotlinx.coroutines.launch

internal class LogDetailDialogViewModel : ViewModel() {

    private val _isShareButtonEnabled = MutableLiveData(true)
    val isShareButtonEnabled: LiveData<Boolean> = _isShareButtonEnabled

    fun shareLogs(activity: Activity?, content: String, timestamp: Long, id: String) {
        if (_isShareButtonEnabled.value == true) {
            viewModelScope.launch {
                _isShareButtonEnabled.postValue(false)
                activity?.createAndShareLogFile(
                    fileName = "${BeagleCore.implementation.behavior.getLogFileName(timestamp, id)}.txt",
                    content = content
                )
                _isShareButtonEnabled.postValue(true)
            }
        }
    }
}