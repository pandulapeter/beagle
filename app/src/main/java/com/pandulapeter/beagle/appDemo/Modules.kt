package com.pandulapeter.beagle.appDemo

import com.pandulapeter.beagle.appDemo.data.ModuleRepository
import com.pandulapeter.beagle.appDemo.data.NetworkingManager
import com.pandulapeter.beagle.appDemo.feature.main.about.AboutViewModel
import com.pandulapeter.beagle.appDemo.feature.main.about.licences.LicencesViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.AuthenticationViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.featureToggles.FeatureTogglesViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.NetworkRequestInterceptorViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.SimpleSetupViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.staticData.StaticDataViewModel
import com.pandulapeter.beagle.appDemo.feature.main.playground.PlaygroundViewModel
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.AddModuleViewModel
import com.pandulapeter.beagle.appDemo.feature.main.setup.SetupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val dataModule = module {
    single { NetworkingManager() }
    single { ModuleRepository() }
}

private val featureModule = module {
    viewModel { SetupViewModel() }
    viewModel { ExamplesViewModel() }
    viewModel { SimpleSetupViewModel() }
    viewModel { StaticDataViewModel() }
    viewModel { FeatureTogglesViewModel() }
    viewModel { AuthenticationViewModel() }
    viewModel { NetworkRequestInterceptorViewModel(get()) }
    viewModel { PlaygroundViewModel(get()) }
    viewModel { AddModuleViewModel(get()) }
    viewModel { AboutViewModel() }
    viewModel { LicencesViewModel() }
}

val modules = dataModule + featureModule