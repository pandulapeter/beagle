package com.pandulapeter.debugMenu

import com.pandulapeter.debugMenuCore.DebugMenuInterceptor
import okhttp3.Interceptor
import okhttp3.Response

class DebugMenuInterceptor : DebugMenuInterceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}