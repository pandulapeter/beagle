package com.pandulapeter.beagle.appDemo.feature.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.BeagleDemoActivity
import com.pandulapeter.beagle.appDemo.utils.AutoClearedValue

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutResourceId: Int,
    private val isRoot: Boolean = true,
    @StringRes private val titleResourceId: Int? = null
) : Fragment() {

    protected var binding by AutoClearedValue<B>()
        private set
    private val currentFragment get() = childFragmentManager.findFragmentById(R.id.fragment_container) as? BaseFragment<*>?

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DataBindingUtil.inflate<B>(inflater, layoutResourceId, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            binding = it
        }.root

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleResourceId?.let { titleResourceId -> (activity as? BeagleDemoActivity?)?.updateToolbar(!isRoot, getString(titleResourceId)) }
    }

    open fun onBackPressed(): Boolean = if (currentFragment?.onBackPressed() != true) childFragmentManager.popBackStackImmediate() else true
}