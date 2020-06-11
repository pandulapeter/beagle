package com.pandulapeter.beagle.appDemo.feature.shared

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.BR
import com.pandulapeter.beagle.common.contracts.module.Module

abstract class ViewModelFragment<B : ViewDataBinding, VM : ViewModel>(
    @LayoutRes layoutResourceId: Int,
    @StringRes titleResourceId: Int?,
    isRoot: Boolean
) : BaseFragment<B>(layoutResourceId, titleResourceId, isRoot) {

    protected abstract val viewModel: VM

    protected abstract fun getBeagleModules(): List<Module<*>>

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        refreshBeagle()
    }

    protected fun refreshBeagle() = Beagle.setModules(*getBeagleModules().toTypedArray())
}