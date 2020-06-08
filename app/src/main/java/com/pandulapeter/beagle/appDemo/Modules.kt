package com.pandulapeter.beagle.appDemo

import com.pandulapeter.beagle.appDemo.feature.main.about.AboutViewModel
import com.pandulapeter.beagle.appDemo.feature.main.home.HomeViewModel
import com.pandulapeter.beagle.appDemo.feature.main.playground.PlaygroundViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val featureModule = module {
    viewModel { HomeViewModel() }
    viewModel { PlaygroundViewModel() }
    viewModel { AboutViewModel() }
}

val modules = featureModule