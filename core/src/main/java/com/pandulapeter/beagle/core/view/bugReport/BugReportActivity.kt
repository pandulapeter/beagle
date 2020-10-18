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
import com.pandulapeter.beagle.core.util.LogEntry
import com.pandulapeter.beagle.core.util.NetworkLogEntry
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
                    logLabelSectionsToShow = arguments.logLabelSectionsToShow,
                    descriptionTemplate = arguments.descriptionTemplate
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
            onMediaFileSelected = ::showMediaPreviewDialog,
            onMediaFileLongTapped = viewModel::onMediaFileLongTapped,
            onNetworkLogSelected = { id -> BeagleCore.implementation.getNetworkLogEntries().firstOrNull { it.id == id }?.let(::showNetworkLogDetailDialog) },
            onNetworkLogLongTapped = viewModel::onNetworkLogLongTapped,
            onShowMoreNetworkLogsTapped = viewModel::onShowMoreNetworkLogsTapped,
            onLogSelected = { id, label -> BeagleCore.implementation.getLogEntries(label).firstOrNull { it.id == id }?.let(::showLogDetailDialog) },
            onLogLongTapped = viewModel::onLogLongTapped,
            onShowMoreLogsTapped = viewModel::onShowMoreLogsTapped
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

    fun refresh() = viewModel.refresh()

    private fun showMediaPreviewDialog(fileName: String) = MediaPreviewDialogFragment.show(supportFragmentManager, fileName)

    private fun showNetworkLogDetailDialog(entry: NetworkLogEntry) = BeagleCore.implementation.showNetworkEventDialog(
        isOutgoing = entry.isOutgoing,
        url = entry.url,
        payload = entry.payload,
        headers = entry.headers,
        duration = entry.duration,
        timestamp = entry.timestamp,
        id = entry.id
    )

    private fun showLogDetailDialog(entry: LogEntry) = BeagleCore.implementation.showDialog(
        content = entry.getFormattedContents(BeagleCore.implementation.appearance.logTimestampFormatter),
        timestamp = entry.timestamp,
        id = entry.id
    )

    companion object {
        private const val ARGUMENTS = "arguments"
        private var Bundle.shouldShowGallerySection by BundleArgumentDelegate.Boolean("shouldShowGallerySection")
        private var Bundle.shouldShowNetworkLogsSection by BundleArgumentDelegate.Boolean("shouldShowNetworkLogsSection")
        private var Bundle.logLabelSectionsToShow by BundleArgumentDelegate.StringList("logLabelSectionsToShow")
        private var Bundle.descriptionTemplate by BundleArgumentDelegate.String("descriptionTemplate")

        fun newIntent(
            context: Context,
            shouldShowGallerySection: Boolean,
            shouldShowNetworkLogsSection: Boolean,
            logLabelSectionsToShow: List<String?>,
            descriptionTemplate: String
        ) = Intent(context, BugReportActivity::class.java).putExtra(ARGUMENTS, Bundle().also {
            it.shouldShowGallerySection = shouldShowGallerySection
            it.shouldShowNetworkLogsSection = shouldShowNetworkLogsSection
            it.logLabelSectionsToShow = logLabelSectionsToShow
            it.descriptionTemplate = descriptionTemplate
        })
    }
}