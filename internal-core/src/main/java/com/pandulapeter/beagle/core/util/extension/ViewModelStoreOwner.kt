package com.pandulapeter.beagle.core.util.extension

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

internal inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModel(): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { ViewModelProvider(this)[T::class.java] }