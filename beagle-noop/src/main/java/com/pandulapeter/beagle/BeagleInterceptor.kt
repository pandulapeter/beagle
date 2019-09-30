package com.pandulapeter.beagle

import com.pandulapeter.beagleCore.contracts.BeagleInterceptorContract
import okhttp3.Interceptor
import okhttp3.Response

object BeagleInterceptor : BeagleInterceptorContract {

    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}