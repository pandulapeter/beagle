package com.pandulapeter.beagle.utils.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import com.pandulapeter.beagle.utils.consume

inline fun View.waitForPreDraw(crossinline block: () -> Unit) = with(viewTreeObserver) {
    addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw() = consume {
            block()
            viewTreeObserver.removeOnPreDrawListener(this)
        }
    })
}

fun View.hideKeyboard() {
    clearFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        0
    )
}

fun View.showKeyboard() {
    requestFocus()
    post { (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, 0) }
}

val View.inflater: LayoutInflater get() = context.inflater