package com.pandulapeter.beagle

import com.pandulapeter.beagleCore.contracts.BeagleNetworkInterceptorContract
import okhttp3.Interceptor
import okhttp3.Response

object BeagleNetworkInterceptor : BeagleNetworkInterceptorContract {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}