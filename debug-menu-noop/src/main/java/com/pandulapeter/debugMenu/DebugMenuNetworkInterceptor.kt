package com.pandulapeter.debugMenu

import com.pandulapeter.debugMenuCore.contracts.DebugMenuNetworkInterceptorContract
import okhttp3.Interceptor
import okhttp3.Response

object DebugMenuNetworkInterceptor : DebugMenuNetworkInterceptorContract {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}