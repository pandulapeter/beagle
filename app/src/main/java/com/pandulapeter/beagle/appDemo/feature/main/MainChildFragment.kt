package com.pandulapeter.beagle.appDemo.feature.main

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.google.android.material.transition.MaterialFadeThrough
import com.pandulapeter.beagle.appDemo.feature.shared.ViewModelFragment

abstract class MainChildFragment<B : ViewDataBinding, VM : ViewModel>(
    @LayoutRes layoutResourceId: Int,
    @StringRes titleResourceId: Int
) : ViewModelFragment<B, VM>(layoutResourceId, titleResourceId, true) {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }
}