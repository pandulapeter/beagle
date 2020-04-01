package com.pandulapeter.beagleExample.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

inline fun <T> Call<T>.executeRequest(crossinline onSuccess: (T) -> Unit, crossinline onError: (Boolean) -> Unit) {
    enqueue(object : Callback<T> {

        override fun onResponse(call: Call<T>?, response: Response<T>?) {
            val body = response?.body()
            if (body == null || !response.isSuccessful) {
                onError(false)
            } else {
                onSuccess(body)
            }
        }

        override fun onFailure(call: Call<T>?, t: Throwable?) = onError(true)
    })
}

fun View.showKeyboard() {
    requestFocus()
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(this, 0)
}