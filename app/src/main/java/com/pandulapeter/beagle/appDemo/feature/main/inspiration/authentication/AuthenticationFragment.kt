package com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication

import android.os.Bundle
import android.view.View
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.InspirationDetailFragment
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.list.AuthenticationAdapter
import com.pandulapeter.beagle.appDemo.feature.main.inspiration.authentication.list.AuthenticationListItem
import com.pandulapeter.beagle.common.contracts.module.Module
import com.pandulapeter.beagle.modules.ItemListModule
import com.pandulapeter.beagle.modules.SwitchModule
import org.koin.android.ext.android.inject

class AuthenticationFragment : InspirationDetailFragment<AuthenticationViewModel, AuthenticationListItem>(R.string.case_study_authentication_title) {

    override val viewModel by inject<AuthenticationViewModel>()
    private val triggerAutomaticallySwitch by lazy {
        SwitchModule(
            id = TRIGGER_AUTOMATICALLY_SWITCH_ID,
            text = getString(R.string.case_study_authentication_trigger_automatically_switch),
            shouldBePersisted = true,
            onValueChanged = {}
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (triggerAutomaticallySwitch.getCurrentValue(Beagle) == true) {
            binding.root.postDelayed({ Beagle.show() }, 500)
        }
    }

    override fun createAdapter() = AuthenticationAdapter()

    override fun getBeagleModules(): List<Module<*>> = listOf(
        triggerAutomaticallySwitch,
        ItemListModule(
            title = getString(R.string.case_study_authentication_test_accounts),
            isExpandedInitially = true,
            items = listOf(
                TestAccount("testaccount1@company.com", "password1"),
                TestAccount("testaccount2@company.com", "password2"),
                TestAccount("testaccount3@company.com", "password3"),
                TestAccount("testaccount4@company.com", "password4"),
                TestAccount("testaccount5@company.com", "password5"),
                TestAccount("testaccount6@company.com", "password6"),
                TestAccount("testaccount7@company.com", "password7"),
                TestAccount("testaccount8@company.com", "password8")
            ),
            onItemSelected = { testAccount ->
                viewModel.updateItems(testAccount.email, testAccount.password)
                Beagle.hide()
            }
        )
    )

    companion object {
        const val TRIGGER_AUTOMATICALLY_SWITCH_ID = "triggerAutomatically"

        fun newInstance() = AuthenticationFragment()
    }
}