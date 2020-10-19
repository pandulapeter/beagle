package com.pandulapeter.beagle.appDemo.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Context.animatedDrawable(@DrawableRes drawableId: Int) = AnimatedVectorDrawableCompat.create(this, drawableId)!!

fun View.showSnackbar(@StringRes messageResourceId: Int) =
    Snackbar.make(this, messageResourceId, Snackbar.LENGTH_SHORT).show()

fun View.openUrl(url: String) = Intent(Intent.ACTION_VIEW, Uri.parse(url)).let { intent ->
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        showSnackbar(R.string.app_not_found)
    }
}

inline fun <reified T : Fragment> FragmentManager.handleReplace(
    tag: String = T::class.java.name,
    addToBackStack: Boolean = false,
    sharedElements: List<View>? = null,
    transitionType: TransitionType? = TransitionType.SIBLING,
    @IdRes containerId: Int = R.id.fragment_container,
    crossinline newInstance: () -> T
) {
    beginTransaction().apply {
        val currentFragment = findFragmentById(containerId)
        (currentFragment as? ListFragment<*, *>?)?.blockGestures()
        val newFragment = findFragmentByTag(tag) ?: newInstance()
        when (transitionType) {
            TransitionType.SIBLING -> {
                currentFragment?.let {
                    currentFragment.exitTransition = MaterialFadeThrough()
                    currentFragment.reenterTransition = MaterialFadeThrough()
                    newFragment.enterTransition = MaterialFadeThrough()
                    newFragment.returnTransition = MaterialFadeThrough()
                }
            }
            TransitionType.MODAL -> {
                if (sharedElements.isNullOrEmpty()) { // || !isContainerTransformSupported) {
                    currentFragment?.exitTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
                    currentFragment?.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
                    newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
                    newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
                } else {
                    currentFragment?.exitTransition = Hold()
                    currentFragment?.reenterTransition = Hold()
                }
            }
            null -> Unit
        }
        sharedElements?.forEach { sharedElement ->
            ViewCompat.getTransitionName(sharedElement)?.let { addSharedElement(sharedElement, it) }
        }
        replace(containerId, newFragment, tag)
        if (addToBackStack) {
            addToBackStack(null)
        }
        setReorderingAllowed(true)
        commitAllowingStateLoss()
    }
}

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline onChanged: (T) -> Unit) =
    Observer<T> { t -> onChanged.invoke(t) }.also {
        observe(owner, it)
    }

@set:BindingAdapter("android:visibility")
var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }