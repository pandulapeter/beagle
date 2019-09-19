package com.pandulapeter.debugMenu

import com.pandulapeter.debugMenuCore.DebugMenuInterceptorContract
import okhttp3.Interceptor
import okhttp3.Response

object DebugMenuInterceptor : DebugMenuInterceptorContract {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}