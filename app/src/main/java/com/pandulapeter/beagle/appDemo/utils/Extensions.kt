package com.pandulapeter.beagle.appDemo.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.pandulapeter.beagle.appDemo.R

fun Fragment.showToast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun Fragment.showToast(@StringRes messageResourceId: Int) = showToast(getString(messageResourceId))

fun Context.color(@ColorRes colorResourceId: Int) = ContextCompat.getColor(this, colorResourceId)

fun Context.dimension(@DimenRes dimensionResourceId: Int) = resources.getDimensionPixelSize(dimensionResourceId)

inline fun <reified T : Fragment> FragmentManager.handleReplace(
    tag: String = T::class.java.name,
    addToBackStack: Boolean = false,
    sharedElements: List<View>? = null,
    @IdRes containerId: Int = R.id.fragment_container,
    crossinline newInstance: () -> T
) {
    beginTransaction().apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElements?.forEach { sharedElement ->
                addSharedElement(sharedElement, sharedElement.transitionName)
            }
        }
        replace(containerId, findFragmentByTag(tag) ?: newInstance(), tag)
        if (addToBackStack) {
            addToBackStack(null)
        }
        setReorderingAllowed(true)
        commitAllowingStateLoss()
    }
}

@BindingAdapter("transitionName")
fun View.setTransitionName(@StringRes stringResourceId: Int) = setTransitionNameCompat(context.getString(stringResourceId))

@BindingAdapter("transitionName")
fun View.setTransitionNameCompat(transitionName: String) = ViewCompat.setTransitionName(this, transitionName)

inline fun View.waitForLayout(crossinline block: () -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            block()
            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

fun Activity.updateSystemBars() = window.run {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_VISIBLE or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline onChanged: (T) -> Unit) = Observer<T> { t -> onChanged.invoke(t) }.also {
    observe(owner, it)
}

@set:BindingAdapter("android:visibility")
var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }