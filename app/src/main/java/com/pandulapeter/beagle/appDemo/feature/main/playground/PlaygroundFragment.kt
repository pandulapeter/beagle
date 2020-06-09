package com.pandulapeter.beagle.appDemo.feature.main.playground

import android.os.Bundle
import android.view.View
import com.google.android.material.transition.MaterialFadeThrough
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.databinding.FragmentPlaygroundBinding
import com.pandulapeter.beagle.appDemo.feature.shared.BaseViewModelFragment
import com.pandulapeter.beagle.modules.TextModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaygroundFragment : BaseViewModelFragment<FragmentPlaygroundBinding, PlaygroundViewModel>(R.layout.fragment_playground, true, R.string.playground_title) {

    override val viewModel by viewModel<PlaygroundViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBeagle()
    }

    private fun setupBeagle() = Beagle.setModules(TextModule(text = "Work in progress"))

    companion object {
        fun newInstance() = PlaygroundFragment()
    }
}