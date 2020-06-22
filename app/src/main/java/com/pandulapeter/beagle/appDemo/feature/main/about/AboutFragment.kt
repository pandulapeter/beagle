package com.pandulapeter.beagle.appDemo.feature.main.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.BuildConfig
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.about.licences.LicencesFragment
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutAdapter
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutListItem
import com.pandulapeter.beagle.appDemo.feature.shared.ListFragment
import com.pandulapeter.beagle.appDemo.utils.TransitionType
import com.pandulapeter.beagle.appDemo.utils.handleReplace
import com.pandulapeter.beagle.appDemo.utils.openUrl
import com.pandulapeter.beagle.appDemo.utils.showSnackbar
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.HeaderModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class AboutFragment : ListFragment<AboutViewModel, AboutListItem>(R.string.about_title) {

    override val viewModel by viewModel<AboutViewModel>()

    override fun createAdapter() = AboutAdapter(viewModel.viewModelScope) { uiModel ->
        when (uiModel.textResourceId) {
            R.string.about_github -> openGitHubRepository()
            R.string.about_google_play -> openStoreListing()
            R.string.about_share -> openShareSheet()
            R.string.about_contact -> openEmailComposer()
            R.string.about_donate -> binding.recyclerView.showSnackbar(R.string.coming_soon) //TODO
            R.string.about_open_source -> navigateToLicences()
        }
    }

    override fun getBeagleModules(): List<Module<*>> = listOf(
        HeaderModule(
            title = getString(R.string.app_name),
            subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            text = "Built on ${BuildConfig.BUILD_DATE}"
        )
    )

    private fun openGitHubRepository() = binding.recyclerView.openUrl(GITHUB_URL)

    private fun openStoreListing() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$PACKAGE_NAME")))
        } catch (_: ActivityNotFoundException) {
            binding.root.openUrl("https://play.google.com/store/apps/details?id=$PACKAGE_NAME")
        }
    }

    private fun openShareSheet() = startActivity(
        Intent.createChooser(
            Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
            }.putExtra(Intent.EXTRA_TEXT, getString(R.string.about_share_text, GITHUB_URL)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), null
        )
    )

    private fun openEmailComposer() = startActivity(
        Intent.createChooser(
            Intent().apply {
                action = Intent.ACTION_SENDTO
                type = "text/plain"
                data = Uri.parse("mailto:$EMAIL_ADDRESS?subject=${Uri.encode("Beagle")}")
            }.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), null
        )
    )

    private fun navigateToLicences() {
        parentFragmentManager.handleReplace(
            transitionType = TransitionType.MODAL,
            addToBackStack = true,
            newInstance = LicencesFragment.Companion::newInstance
        )
    }

    companion object {
        private const val PACKAGE_NAME = "com.pandulapeter.beagleExample"
        private const val EMAIL_ADDRESS = "pandulapeter@gmail.com"
        const val GITHUB_URL = "https://github.com/pandulapeter/beagle"

        fun newInstance() = AboutFragment()
    }
}