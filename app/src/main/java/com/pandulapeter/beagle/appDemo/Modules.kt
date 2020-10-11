package com.pandulapeter.beagle.appDemo

import com.pandulapeter.beagle.appDemo.data.ModuleRepository
import com.pandulapeter.beagle.appDemo.data.SongRepository
import com.pandulapeter.beagle.appDemo.data.networking.ktor.KtorRemoteSongSource
import com.pandulapeter.beagle.appDemo.data.networking.retrofit.OkHttpRemoteSongSource
import com.pandulapeter.beagle.appDemo.feature.main.about.AboutViewModel
import com.pandulapeter.beagle.appDemo.feature.main.about.licences.LicencesViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.analytics.AnalyticsViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.authentication.AuthenticationViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.mockDataGenerator.MockDataGeneratorViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.navigation.NavigationViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.networkRequestInterceptor.NetworkRequestInterceptorViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.overlay.OverlayViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.simpleSetup.SimpleSetupViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.staticData.StaticDataViewModel
import com.pandulapeter.beagle.appDemo.feature.main.examples.valueWrappers.ValueWrappersViewModel
import com.pandulapeter.beagle.appDemo.feature.main.playground.PlaygroundViewModel
import com.pandulapeter.beagle.appDemo.feature.main.playground.addModule.AddModuleViewModel
import com.pandulapeter.beagle.appDemo.feature.main.setup.SetupViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val dataModule = module {
    single { OkHttpRemoteSongSource() }
    single { KtorRemoteSongSource() }
    single { SongRepository(get(), get()) }
    single { ModuleRepository() }
}

private val featureModule = module {
    viewModel { SetupViewModel() }
    viewModel { ExamplesViewModel() }
    viewModel { SimpleSetupViewModel() }
    viewModel { StaticDataViewModel() }
    viewModel { ValueWrappersViewModel() }
    viewModel { AuthenticationViewModel() }
    viewModel { NetworkRequestInterceptorViewModel(get()) }
    viewModel { AnalyticsViewModel() }
    viewModel { MockDataGeneratorViewModel() }
    viewModel { OverlayViewModel() }
    viewModel { NavigationViewModel() }
    viewModel { PlaygroundViewModel(get()) }
    viewModel { AddModuleViewModel(get()) }
    viewModel { AboutViewModel(androidContext()) }
    viewModel { LicencesViewModel() }
}

val modules = dataModule + featureModule