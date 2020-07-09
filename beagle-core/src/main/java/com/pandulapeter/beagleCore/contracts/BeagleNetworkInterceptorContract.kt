package com.pandulapeter.beagleCore.contracts

import okhttp3.Interceptor
import okhttp3.Response

@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
interface BeagleNetworkInterceptorContract : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}