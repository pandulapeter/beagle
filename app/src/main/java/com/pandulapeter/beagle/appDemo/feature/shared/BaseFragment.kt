package com.pandulapeter.beagle.appDemo.feature.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.pandulapeter.beagle.appDemo.R

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutResourceId: Int
) : Fragment() {

    private var _binding: B? = null
    protected val binding: B get() = _binding ?: throw IllegalStateException("Trying to access the binding outside of the view lifecycle")
    protected val currentFragment get() = childFragmentManager.findFragmentById(R.id.fragment_container) as? BaseFragment<*>?
    protected val activityFragmentManager get() = activity?.supportFragmentManager

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DataBindingUtil.inflate<B>(inflater, layoutResourceId, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            _binding = it
        }.root

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    final override fun startPostponedEnterTransition() {
        super.startPostponedEnterTransition()
        parentFragment?.startPostponedEnterTransition()
    }

    open fun onBackPressed(): Boolean =
        if (currentFragment?.onBackPressed() != true) childFragmentManager.popBackStackImmediate() else true
}