package com.pandulapeter.beagle.appDemo.feature.main.examples.navigation

import androidx.lifecycle.viewModelScope
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.examples.ExamplesDetailFragment
import com.pandulapeter.beagle.appDemo.feature.shared.list.BaseAdapter
import com.pandulapeter.beagle.appDemo.feature.shared.list.ListItem
import com.pandulapeter.beagle.appDemo.utils.createTextModule
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.DividerModule
import com.pandulapeter.beagle.modules.HeaderModule
import com.pandulapeter.beagle.modules.PaddingModule
import org.koin.androidx.viewmodel.ext.android.viewModel

class NavigationFragment : ExamplesDetailFragment<NavigationViewModel, ListItem>(R.string.case_study_navigation_title) {

    override val viewModel by viewModel<NavigationViewModel>()
    private var currentPage
        get() = viewModel.currentPage
        set(value) {
            viewModel.currentPage = value
            refreshBeagle()
        }

    override fun createAdapter() = object : BaseAdapter<ListItem>(
        scope = viewModel.viewModelScope
    ) {}

    override fun getBeagleModules(): List<Module<*>> = mutableListOf<Module<*>>().apply {
        addHeaderSection()
        when (currentPage) {
            NavigationViewModel.Page.MAIN -> addMainSection()
            NavigationViewModel.Page.SECTION_1 -> addSection1()
            NavigationViewModel.Page.SECTION_2 -> addSection2()
            NavigationViewModel.Page.SECTION_3 -> addSection3()
        }
    }

    private fun MutableList<Module<*>>.addHeaderSection() {
        add(
            HeaderModule(
                title = R.string.case_study_navigation_header_title,
                subtitle = R.string.case_study_navigation_header_subtitle,
                text = R.string.case_study_navigation_header_text
            )
        )
        add(DividerModule("divider"))
        add(PaddingModule())
    }

    private fun MutableList<Module<*>>.addMainSection() {
        add(
            createTextModule(
                textResourceId = R.string.case_study_navigation_header_section_1,
                onItemSelected = { currentPage = NavigationViewModel.Page.SECTION_1 }
            )
        )
        add(
            createTextModule(
                textResourceId = R.string.case_study_navigation_header_section_2,
                onItemSelected = { currentPage = NavigationViewModel.Page.SECTION_2 }
            )
        )
        add(
            createTextModule(
                textResourceId = R.string.case_study_navigation_header_section_3,
                onItemSelected = { currentPage = NavigationViewModel.Page.SECTION_3 }
            )
        )
    }

    private fun MutableList<Module<*>>.addCloseButton() {
        add(
            createTextModule(
                textResourceId = R.string.case_study_navigation_close_section,
                icon = R.drawable.ic_close,
                onItemSelected = { currentPage = NavigationViewModel.Page.MAIN }
            )
        )
    }

    private fun MutableList<Module<*>>.addSection1() {
        addCloseButton()
        add(createTextModule(R.string.case_study_navigation_header_section_1_detail))
    }

    private fun MutableList<Module<*>>.addSection2() {
        addCloseButton()
        add(createTextModule(R.string.case_study_navigation_header_section_2_detail))
    }

    private fun MutableList<Module<*>>.addSection3() {
        addCloseButton()
        add(createTextModule(R.string.case_study_navigation_header_section_3_detail))
    }

    companion object {
        fun newInstance() = NavigationFragment()
    }
}