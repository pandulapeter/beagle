package com.pandulapeter.beagle.appDemo

import com.pandulapeter.beagle.appDemo.feature.main.about.AboutViewModel
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationViewModel
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.AuthenticationViewModel
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.basicSetup.BasicSetupViewModel
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.featureToggles.FeatureTogglesViewModel
import com.pandulapeter.beagle.appDemo.feature.main.playground.PlaygroundViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val featureModule = module {
    viewModel { InspirationViewModel() }
    viewModel { BasicSetupViewModel() }
    viewModel { AuthenticationViewModel() }
    viewModel { FeatureTogglesViewModel() }
    viewModel { PlaygroundViewModel() }
    viewModel { AboutViewModel() }
}

val modules = featureModule