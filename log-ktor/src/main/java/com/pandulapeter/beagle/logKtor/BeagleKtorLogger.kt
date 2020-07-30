package com.pandulapeter.beagle.logKtor

import com.pandulapeter.beagle.commonBase.BeagleNetworkLoggerContract

object BeagleKtorLogger : BeagleNetworkLoggerContract by KtorLoggerImplementation()