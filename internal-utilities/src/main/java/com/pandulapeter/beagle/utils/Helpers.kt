package com.pandulapeter.beagle.utils

import androidx.lifecycle.MutableLiveData

fun consume(callback: () -> Unit) = true.also { callback() }

fun <T> mutableLiveDataOf(initialValue: T) = MutableLiveData<T>().apply {
    value = initialValue
}