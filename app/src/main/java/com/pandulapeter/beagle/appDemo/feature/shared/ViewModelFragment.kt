package com.pandulapeter.beagle.appDemo.feature.shared

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.BR
import com.pandulapeter.beagle.common.contracts.module.Module

abstract class ViewModelFragment<B : ViewDataBinding, VM : ViewModel>(
    @LayoutRes layoutResourceId: Int,
    @StringRes protected val titleResourceId: Int,
    private val isRoot: Boolean
) : BaseFragment<B>(layoutResourceId) {

    protected abstract val viewModel: VM
    protected abstract val appBar: AppBarView

    @ColorInt
    protected open val backgroundColor = Color.TRANSPARENT

    protected abstract fun getBeagleModules(): List<Module<*>>

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        binding.root.setBackgroundColor(backgroundColor)
        appBar.setup(titleResourceId, isRoot, requireActivity())
        refreshBeagle()
    }

    protected fun refreshBeagle() = Beagle.setModules(*getBeagleModules().toTypedArray())
}