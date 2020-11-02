package com.pandulapeter.beagle.appDemo.feature.main.about

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.SkuDetailsParams
import com.pandulapeter.beagle.appDemo.BuildConfig
import com.pandulapeter.beagle.appDemo.R
import com.pandulapeter.beagle.appDemo.feature.main.about.list.AboutListItem
import com.pandulapeter.beagle.appDemo.feature.main.about.list.ClickableItemViewHolder
import com.pandulapeter.beagle.appDemo.feature.shared.ListViewModel
import com.pandulapeter.beagle.appDemo.feature.shared.list.TextViewHolder

class AboutViewModel(context: Context) : ListViewModel<AboutListItem>(), PurchasesUpdatedListener {

    private val billingClient by lazy {
        BillingClient
            .newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()
    }
    var snackbarMessage = MutableLiveData<Int?>()

    override val items: LiveData<List<AboutListItem>> = MutableLiveData(
        mutableListOf<AboutListItem>().apply {
            add(TextViewHolder.UiModel(R.string.about_description))
            add(ClickableItemViewHolder.UiModel(R.string.about_github, R.drawable.ic_github))
            add(ClickableItemViewHolder.UiModel(R.string.about_article, R.drawable.ic_article))
            add(ClickableItemViewHolder.UiModel(R.string.about_google_play, R.drawable.ic_google_play))
            add(ClickableItemViewHolder.UiModel(R.string.about_share, R.drawable.ic_share))
            add(ClickableItemViewHolder.UiModel(R.string.about_contact, R.drawable.ic_contact))
            add(ClickableItemViewHolder.UiModel(R.string.about_open_source, R.drawable.ic_open_source))
            @Suppress("ConstantConditionIf")
            if (BuildConfig.APPLICATION_ID == AboutFragment.PACKAGE_NAME) {
                add(ClickableItemViewHolder.UiModel(R.string.about_donate, R.drawable.ic_donate))
            }
        }
    )

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK && !purchases.isNullOrEmpty()) {
            purchases[0].let { purchase ->
                billingClient.consumeAsync(ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()) { result, _ ->
                    if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                        snackbarMessage.value = R.string.about_donate_success
                    }
                }
            }
        } else if (result.responseCode != BillingClient.BillingResponseCode.USER_CANCELED) {
            snackbarMessage.value = R.string.about_donate_error
        }
    }

    fun startPurchaseFlow(activity: Activity) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    billingClient.querySkuDetailsAsync(
                        SkuDetailsParams
                            .newBuilder()
                            .setSkusList(listOf("buy_me_a_beer"))
                            .setType(BillingClient.SkuType.INAPP)
                            .build()
                    ) { _, skuDetails ->
                        if (skuDetails.isNullOrEmpty()) {
                            snackbarMessage.value = R.string.about_donate_error
                        } else {
                            billingClient.launchBillingFlow(
                                activity, BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetails.first())
                                    .build()
                            )
                        }
                    }
                }
            }

            override fun onBillingServiceDisconnected() = Unit
        })
    }
}