package com.pandulapeter.beagle.core.view.bugReport

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportAdapter
import com.pandulapeter.beagle.core.view.gallery.MediaPreviewDialogFragment
import com.pandulapeter.beagle.utils.BundleArgumentDelegate
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.tintedDrawable

internal class BugReportActivity : AppCompatActivity() {

    @Suppress("UNCHECKED_CAST")
    private val viewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = intent.getBundleExtra(ARGUMENTS)!!.let { arguments ->
                BugReportViewModel(
                    context = applicationContext,
                    shouldShowGallerySection = arguments.shouldShowGallerySection,
                    shouldShowNetworkLogsSection = arguments.shouldShowNetworkLogsSection,
                    logTagSectionsToShow = arguments.logTagSectionsToShow
                ) as T
            }
        }).get(BugReportViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beagle_activity_bug_report)
        supportActionBar?.hide()
        findViewById<Toolbar>(R.id.beagle_toolbar).apply {
            val textColor = colorResource(android.R.attr.textColorPrimary)
            setNavigationOnClickListener { supportFinishAfterTransition() }
            navigationIcon = tintedDrawable(R.drawable.beagle_ic_close, textColor)
            title = text(BeagleCore.implementation.appearance.bugReportTexts.title)
        }
        val recyclerView = findViewById<RecyclerView>(R.id.beagle_recycler_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            val contentPadding by lazy { dimension(R.dimen.beagle_content_padding) }
            val bottomNavigationOverlay = findViewById<View>(R.id.beagle_bottom_navigation_overlay)
            bottomNavigationOverlay.setBackgroundColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor else Color.BLACK)
            window.decorView.run {
                setOnApplyWindowInsetsListener { _, insets ->
                    onApplyWindowInsets(insets).also {
                        recyclerView.setPadding(
                            0,
                            contentPadding,
                            0,
                            it.systemWindowInsetBottom + contentPadding
                        )
                        bottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = it.systemWindowInsetBottom } }
                    }
                }
                requestApplyInsets()
            }
        }
        val bugReportAdapter = BugReportAdapter(
            onSendButtonPressed = viewModel::onSendButtonPressed,
            onMediaFileSelected = { showPreviewDialog(viewModel.getFileName(it)) },
            onMediaFileLongTapped = viewModel::onMediaFileLongTapped
        )
        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = bugReportAdapter
        }
        viewModel.items.observe(this, bugReportAdapter::submitList)
        findViewById<ProgressBar>(R.id.beagle_progress_bar).let { progressBar ->
            viewModel.shouldShowLoadingIndicator.observe(this) { progressBar.visible = it }
        }
    }

    private fun showPreviewDialog(fileName: String) = MediaPreviewDialogFragment.show(supportFragmentManager, fileName)

    companion object {
        private const val ARGUMENTS = "arguments"
        private var Bundle.shouldShowGallerySection by BundleArgumentDelegate.Boolean("shouldShowGallerySection")
        private var Bundle.shouldShowNetworkLogsSection by BundleArgumentDelegate.Boolean("shouldShowNetworkLogsSection")
        private var Bundle.logTagSectionsToShow by BundleArgumentDelegate.StringList("logTagSectionsToShow")

        fun newIntent(
            context: Context,
            shouldShowGallerySection: Boolean,
            shouldShowNetworkLogsSection: Boolean,
            logTagSectionsToShow: List<String?>
        ) = Intent(context, BugReportActivity::class.java).putExtra(ARGUMENTS, Bundle().also {
            it.shouldShowGallerySection = shouldShowGallerySection
            it.shouldShowNetworkLogsSection = shouldShowNetworkLogsSection
            it.logTagSectionsToShow = logTagSectionsToShow
        })
    }
}