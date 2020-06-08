package com.pandulapeter.beagle.appDemo.feature.shared

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.appDemo.BR

abstract class BaseViewModelFragment<B : ViewDataBinding, VM : ViewModel>(
    @LayoutRes layoutResourceId: Int,
    isRoot: Boolean = true,
    @StringRes titleResourceId: Int? = null
) : BaseFragment<B>(layoutResourceId, isRoot, titleResourceId) {

    protected abstract val viewModel: VM

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
    }
}