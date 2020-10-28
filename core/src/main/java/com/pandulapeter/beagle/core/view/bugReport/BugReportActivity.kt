package com.pandulapeter.beagle.core.view.bugReport

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.pandulapeter.beagle.BeagleCore
import com.pandulapeter.beagle.common.configuration.Text
import com.pandulapeter.beagle.common.configuration.toText
import com.pandulapeter.beagle.core.R
import com.pandulapeter.beagle.core.list.delegates.DeviceInfoDelegate
import com.pandulapeter.beagle.core.list.delegates.LifecycleLogListDelegate
import com.pandulapeter.beagle.core.util.crashLogEntryAdapter
import com.pandulapeter.beagle.core.util.extension.append
import com.pandulapeter.beagle.core.util.extension.shareFile
import com.pandulapeter.beagle.core.util.extension.text
import com.pandulapeter.beagle.core.util.extension.visible
import com.pandulapeter.beagle.core.util.model.CrashLogEntry
import com.pandulapeter.beagle.core.util.model.LifecycleLogEntry
import com.pandulapeter.beagle.core.util.model.LogEntry
import com.pandulapeter.beagle.core.util.model.NetworkLogEntry
import com.pandulapeter.beagle.core.util.restoreModelAdapter
import com.pandulapeter.beagle.core.view.bugReport.list.BugReportAdapter
import com.pandulapeter.beagle.core.view.gallery.MediaPreviewDialogFragment
import com.pandulapeter.beagle.utils.consume
import com.pandulapeter.beagle.utils.extensions.colorResource
import com.pandulapeter.beagle.utils.extensions.dimension
import com.pandulapeter.beagle.utils.extensions.hideKeyboard
import com.pandulapeter.beagle.utils.extensions.tintedDrawable
import com.squareup.moshi.JsonDataException

internal class BugReportActivity : AppCompatActivity() {

    private val crashLogEntryToShow by lazy {
        intent.getStringExtra(CRASH_LOG_ENTRY_TO_SHOW).let { inputString ->
            if (inputString.isNullOrBlank()) null else try {
                crashLogEntryAdapter.fromJson(inputString)
            } catch (_: JsonDataException) {
                null
            }
        }
    }
    private val restoreModel by lazy {
        intent.getStringExtra(RESTORE_MODEL).let { inputString ->
            if (inputString.isNullOrBlank()) null else try {
                restoreModelAdapter.fromJson(inputString)
            } catch (_: JsonDataException) {
                null
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private val viewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T = BeagleCore.implementation.behavior.bugReportingBehavior.run {
                BugReportViewModel(
                    application = application,
                    restoreModel = restoreModel,
                    crashLogEntryToShow = crashLogEntryToShow,
                    buildInformation = buildInformation(application).generateBuildInformation(),
                    deviceInformation = generateDeviceInformation(),
                    textInputTitles = textInputFields.map { it.first },
                    textInputDescriptions = textInputFields.map { it.second }
                ) as T
            }
        }).get(BugReportViewModel::class.java)
    }
    private lateinit var sendButton: MenuItem
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.beagle_recycler_view) }
    private val appBarLayout by lazy { findViewById<AppBarLayout>(R.id.beagle_app_bar) }
    private val onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener { recyclerView.run { post { appBarLayout.setLifted(computeVerticalScrollOffset() != 0) } } }

    override fun onCreate(savedInstanceState: Bundle?) {
        BeagleCore.implementation.appearance.themeResourceId?.let { setTheme(it) }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.beagle_activity_bug_report)
        supportActionBar?.hide()
        val toolbar = findViewById<Toolbar>(R.id.beagle_toolbar).apply {
            val textColor = colorResource(android.R.attr.textColorPrimary)
            setNavigationOnClickListener { supportFinishAfterTransition() }
            navigationIcon = tintedDrawable(R.drawable.beagle_ic_close, textColor)
            title = text(BeagleCore.implementation.appearance.bugReportTexts.title)
            sendButton = menu.findItem(R.id.beagle_send).also {
                it.title = text(BeagleCore.implementation.appearance.bugReportTexts.sendButtonHint)
                it.icon = tintedDrawable(R.drawable.beagle_ic_send, textColor)
            }
            setOnMenuItemClickListener(::onMenuItemClicked)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            val contentPadding by lazy { dimension(R.dimen.beagle_content_padding) }
            val bottomNavigationOverlay = findViewById<View>(R.id.beagle_bottom_navigation_overlay)
            bottomNavigationOverlay.setBackgroundColor(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor else Color.BLACK)
            window.decorView.run {
                setOnApplyWindowInsetsListener { _, insets ->
                    onApplyWindowInsets(insets).also {
                        toolbar.setPadding(it.systemWindowInsetLeft, 0, it.systemWindowInsetRight, 0)
                        recyclerView.setPadding(0, 0, 0, it.systemWindowInsetBottom + contentPadding)
                        bottomNavigationOverlay.run { layoutParams = layoutParams.apply { height = it.systemWindowInsetBottom } }
                    }
                }
                requestApplyInsets()
            }
        }
        val bugReportAdapter = BugReportAdapter(
            onMediaFileSelected = ::showMediaPreviewDialog,
            onMediaFileLongTapped = viewModel::onMediaFileLongTapped,
            onCrashLogSelected = { id -> viewModel.allCrashLogEntries?.firstOrNull { it.id == id }?.let(::showCrashLogDetailDialog) },
            onCrashLogLongTapped = viewModel::onCrashLogLongTapped,
            onNetworkLogSelected = { id -> viewModel.allNetworkLogEntries.firstOrNull { it.id == id }?.let(::showNetworkLogDetailDialog) },
            onNetworkLogLongTapped = viewModel::onNetworkLogLongTapped,
            onLogSelected = { id, label -> BeagleCore.implementation.getLogEntries(label).firstOrNull { it.id == id }?.let(::showLogDetailDialog) },
            onLogLongTapped = viewModel::onLogLongTapped,
            onLifecycleLogSelected = { id -> viewModel.allLifecycleLogEntries.firstOrNull { it.id == id }?.let(::showLifecycleLogDetailDialog) },
            onLifecycleLogLongTapped = viewModel::onLifecycleLogLongTapped,
            onShowMoreTapped = viewModel::onShowMoreTapped,
            onDescriptionChanged = viewModel::onDescriptionChanged,
            onMetadataItemClicked = ::showMetadataDetailDialog,
            onMetadataItemSelectionChanged = viewModel::onMetadataItemSelectionChanged
        )
        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = bugReportAdapter
        }
        viewModel.items.observe(this, bugReportAdapter::submitList)
        findViewById<ProgressBar>(R.id.beagle_progress_bar).let { progressBar ->
            viewModel.shouldShowLoadingIndicator.observe(this) { shouldShowLoadingIndicator ->
                progressBar.visible = shouldShowLoadingIndicator
                if (shouldShowLoadingIndicator) {
                    currentFocus?.hideKeyboard()
                } else {
                    crashLogEntryToShow?.let { crashLogEntryToShow ->
                        if (!intent.getStringExtra(CRASH_LOG_ENTRY_TO_SHOW).isNullOrBlank()) {
                            intent.removeExtra(CRASH_LOG_ENTRY_TO_SHOW)
                            viewModel.onCrashLogLongTapped(crashLogEntryToShow.id)
                            viewModel.allCrashLogEntries?.firstOrNull { it.id == crashLogEntryToShow.id }?.let(::showCrashLogDetailDialog)
                        }
                    }
                }
            }
        }
        viewModel.isSendButtonEnabled.observe(this) {
            sendButton.isEnabled = it
            sendButton.icon.alpha = if (it) 255 else 63
        }
        viewModel.zipFileUriToShare.observe(this) { uri ->
            if (uri != null) {
                shareFile(uri, "application/zip")
                viewModel.zipFileUriToShare.value = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        recyclerView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    override fun onPause() {
        super.onPause()
        recyclerView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun refresh() = viewModel.refresh()

    private fun generateDeviceInformation(): CharSequence {
        var text: CharSequence = ""
        //TODO: Should be configurable
        DeviceInfoDelegate.getDeviceInfo(
            shouldShowManufacturer = true,
            shouldShowModel = true,
            shouldShowResolutionsPx = true,
            shouldShowResolutionsDp = true,
            shouldShowDensity = true,
            shouldShowAndroidVersion = true
        ).also { sections ->
            val lastIndex = sections.lastIndex
            sections.forEachIndexed { index, (keyText, value) ->
                val key = text(keyText)
                text = text.append(SpannableString("$key: $value".let { if (index == lastIndex) it else "$it\n" }).apply {
                    setSpan(StyleSpan(Typeface.BOLD), 0, key.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                })
            }
        }
        return text
    }

    private fun onMenuItemClicked(menuItem: MenuItem) = when (menuItem.itemId) {
        R.id.beagle_send -> consume(viewModel::onSendButtonPressed)
        else -> false
    }

    private fun showMediaPreviewDialog(fileName: String) = MediaPreviewDialogFragment.show(supportFragmentManager, fileName)

    private fun showCrashLogDetailDialog(entry: CrashLogEntry) = BeagleCore.implementation.showDialog(
        content = entry.getFormattedContents(BeagleCore.implementation.appearance.logLongTimestampFormatter).toText(),
        isHorizontalScrollEnabled = true,
        shouldShowShareButton = true,
        timestamp = entry.timestamp,
        id = entry.id,
        fileName = BeagleCore.implementation.behavior.bugReportingBehavior.getCrashLogFileName(entry.timestamp, entry.id)
    )

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
        content = entry.getFormattedContents(BeagleCore.implementation.appearance.logLongTimestampFormatter).toText(),
        timestamp = entry.timestamp,
        id = entry.id
    )

    private fun showLifecycleLogDetailDialog(entry: LifecycleLogEntry) = BeagleCore.implementation.showDialog(
        content = LifecycleLogListDelegate.format(
            entry = entry,
            formatter = BeagleCore.implementation.appearance.logLongTimestampFormatter,
            shouldDisplayFullNames = BeagleCore.implementation.behavior.lifecycleLogBehavior.shouldDisplayFullNames
        ),
        isHorizontalScrollEnabled = false,
        shouldShowShareButton = true,
        timestamp = entry.timestamp,
        id = entry.id,
        fileName = BeagleCore.implementation.behavior.lifecycleLogBehavior.getFileName(entry.timestamp, entry.id)
    )

    private fun showMetadataDetailDialog(type: BugReportViewModel.MetadataType) = BeagleCore.implementation.showDialog(
        content = when (type) {
            BugReportViewModel.MetadataType.BUILD_INFORMATION -> viewModel.buildInformation
            BugReportViewModel.MetadataType.DEVICE_INFORMATION -> viewModel.deviceInformation
        }.toText(),
        shouldShowShareButton = false
    )

    private fun List<Pair<Text, String>>.generateBuildInformation(): CharSequence {
        var text: CharSequence = ""
        forEachIndexed { index, (keyText, value) ->
            val key = text(keyText)
            if (key.isNotBlank() && value.isNotBlank()) {
                text = text.append(SpannableString("$key: $value".let { if (index == lastIndex) it else "$it\n" }).apply {
                    setSpan(StyleSpan(Typeface.BOLD), 0, key.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
                })
            }
        }
        return text
    }

    companion object {
        private const val CRASH_LOG_ENTRY_TO_SHOW = "crashLogEntry"
        private const val RESTORE_MODEL = "restoreModel"

        fun newIntent(
            context: Context,
            crashLogEntryToShowJson: String = "",
            restoreModelJson: String = ""
        ) = Intent(context, BugReportActivity::class.java)
            .putExtra(CRASH_LOG_ENTRY_TO_SHOW, crashLogEntryToShowJson)
            .putExtra(RESTORE_MODEL, restoreModelJson)
    }
}