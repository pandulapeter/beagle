package com.pandulapeter.beagle.appDemo.feature.main.examples.staticData

import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.BuildConfig
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.utils.createButtonModule
import com.pandulapeter.beagle.appDemo.utils.createLongTextModule
import com.pandulapeter.beagle.appDemo.utils.createSectionHeaderModule
import com.pandulapeter.beagle.appDemo.utils.createTextModule
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.HeaderModule
import com.pandulapeter.beagle.modules.KeyValueListModule
import com.pandulapeter.beagle.modules.LoadingIndicatorModule
import com.pandulapeter.beagle.modules.PaddingModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaticDataFragment : ExamplesDetailFragment<StaticDataViewModel, ListItem>(R.string.case_study_static_data_title) {

    override val viewModel by viewModel<StaticDataViewModel>()

    override fun createAdapter() = object : BaseAdapter<ListItem>(
        scope = viewModel.viewModelScope,
        onSectionHeaderSelected = viewModel::onSectionHeaderSelected
    ) {}

    override fun getBeagleModules(): List<Module<*>> = listOf(
        HeaderModule(
            title = "${getString(R.string.app_name)} v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
            subtitle = BuildConfig.APPLICATION_ID,
            text = "Built on ${BuildConfig.BUILD_DATE}\n"
        ),
        createTextModule(R.string.case_study_static_data_module_text_1),
        createTextModule(R.string.case_study_static_data_module_text_2, icon = R.drawable.ic_examples),
        createLongTextModule(R.string.case_study_static_data_module_text_5, R.string.case_study_static_data_module_text_6),
        createSectionHeaderModule(R.string.case_study_static_data_module_section_header_1),
        PaddingModule(),
        PaddingModule(),
        PaddingModule(),
        PaddingModule(),
        createTextModule(R.string.case_study_static_data_module_text_3),
        DividerModule(id = "divider"),
        createTextModule(R.string.case_study_static_data_module_text_4),
        KeyValueListModule(
            id = "keyValueList",
            title = getText(R.string.case_study_static_data_module_key_value_title),
            pairs = listOf(
                getText(R.string.case_study_static_data_module_key_1) to getText(R.string.case_study_static_data_module_value_1),
                getText(R.string.case_study_static_data_module_key_2) to getText(R.string.case_study_static_data_module_value_2),
                getText(R.string.case_study_static_data_module_key_3) to getText(R.string.case_study_static_data_module_value_3)
            )
        ),
        createButtonModule(R.string.case_study_static_data_module_text_7, ::onButtonClicked),
        createTextModule(R.string.case_study_static_data_module_text_8),
        PaddingModule(),
        LoadingIndicatorModule()
    )

    private fun onButtonClicked() = Toast.makeText(requireContext(), R.string.case_study_static_data_button_clicked, Toast.LENGTH_SHORT).show()

    companion object {
        fun newInstance() = StaticDataFragment()
    }
}