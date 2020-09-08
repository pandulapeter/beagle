package com.pandulapeter.beagle.appDemo.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.pandulapeter.beagle.modules.LongTextModule
import com.pandulapeter.beagle.modules.SectionHeaderModule
import com.pandulapeter.beagle.modules.TextModule

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Context.animatedDrawable(@DrawableRes drawableId: Int) = AnimatedVectorDrawableCompat.create(this, drawableId)!!

fun View.hideKeyboard() {
    clearFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(windowToken, 0)
}

fun View.showSnackbar(@StringRes messageResourceId: Int) = Snackbar.make(this, messageResourceId, Snackbar.LENGTH_SHORT).show()

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
        sharedElements?.forEach { sharedElement -> ViewCompat.getTransitionName(sharedElement)?.let { addSharedElement(sharedElement, it) } }
        replace(containerId, newFragment, tag)
        if (addToBackStack) {
            addToBackStack(null)
        }
        setReorderingAllowed(true)
        commitAllowingStateLoss()
    }
}

//val isContainerTransformSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

@BindingAdapter("transitionName")
fun View.setTransitionName(@StringRes stringResourceId: Int) = setTransitionNameCompat(context.getString(stringResourceId))

@BindingAdapter("transitionName")
fun View.setTransitionNameCompat(transitionName: String) = ViewCompat.setTransitionName(this, transitionName)

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

fun Fragment.createTextModule(
    @StringRes textResourceId: Int,
    id: String = "text_$textResourceId"
) = TextModule(
    id = id,
    text = getText(textResourceId)
)

fun Fragment.createLongTextModule(
    @StringRes titleResourceId: Int,
    @StringRes textResourceId: Int,
    id: String = "text_$textResourceId"
) = LongTextModule(
    id = id,
    title = getText(titleResourceId),
    text = getText(textResourceId)
)

fun Fragment.createSectionHeaderModule(
    @StringRes titleResourceId: Int,
    id: String = "sectionHeader_$titleResourceId"
) = SectionHeaderModule(
    id = id,
    title = getText(titleResourceId)
)