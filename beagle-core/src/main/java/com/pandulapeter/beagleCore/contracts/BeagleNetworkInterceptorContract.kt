package com.pandulapeter.beagleCore.contracts

import okhttp3.Interceptor
import okhttp3.Response

interface BeagleNetworkInterceptorContract : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}