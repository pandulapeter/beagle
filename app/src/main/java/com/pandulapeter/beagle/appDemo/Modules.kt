package com.pandulapeter.beagle.appDemo

import com.pandulapeter.beagle.appDemo.data.CaseStudyRepository
import com.pandulapeter.beagle.appDemo.feature.main.about.AboutViewModel
import com.pandulapeter.beagle.appDemo.feature.main.home.HomeViewModel
import com.pandulapeter.beagle.appDemo.feature.main.playground.PlaygroundViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val dataModule = module {
    single { CaseStudyRepository() }
}

private val featureModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { PlaygroundViewModel() }
    viewModel { AboutViewModel() }
}

val modules = dataModule + featureModule