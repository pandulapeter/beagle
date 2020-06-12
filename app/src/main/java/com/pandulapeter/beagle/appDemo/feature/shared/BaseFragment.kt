package com.pandulapeter.beagle.appDemo.feature.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.utils.AutoClearedValue

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutResourceId: Int
) : Fragment() {

    protected var binding by AutoClearedValue<B>()
        private set
    protected val currentFragment get() = childFragmentManager.findFragmentById(R.id.fragment_container) as? BaseFragment<*>?
    protected val activityFragmentManager get() = activity?.supportFragmentManager

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DataBindingUtil.inflate<B>(inflater, layoutResourceId, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            binding = it
        }.root

    final override fun startPostponedEnterTransition() {
        super.startPostponedEnterTransition()
        parentFragment?.startPostponedEnterTransition()
    }

    open fun onBackPressed(): Boolean = if (currentFragment?.onBackPressed() != true) childFragmentManager.popBackStackImmediate() else true
}