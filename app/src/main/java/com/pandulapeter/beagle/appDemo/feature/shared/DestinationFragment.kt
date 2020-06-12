package com.pandulapeter.beagle.appDemo.feature.shared

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.BR
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.utils.color
import com.pandulapeter.beagle.common.contracts.module.Module

abstract class DestinationFragment<B : ViewDataBinding, VM : ViewModel>(
    @LayoutRes layoutResourceId: Int,
    @StringRes protected val titleResourceId: Int,
    @ColorRes private val backgroundColorResourceId: Int = R.color.transparent
) : BaseFragment<B>(layoutResourceId) {

    protected abstract val viewModel: VM
    protected abstract val appBar: AppBarView

    protected abstract fun getBeagleModules(): List<Module<*>>

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setVariable(BR.viewModel, viewModel)
        binding.root.setBackgroundColor(requireContext().color(backgroundColorResourceId))
        appBar.setup(titleResourceId, parentFragmentManager.backStackEntryCount <= 1, requireActivity())
        refreshBeagle()
    }

    protected fun refreshBeagle() = Beagle.setModules(*getBeagleModules().toTypedArray())
}