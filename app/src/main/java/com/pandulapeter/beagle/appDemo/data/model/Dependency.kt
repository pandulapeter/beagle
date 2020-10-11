package com.pandulapeter.beagle.appDemo.data.model

import androidx.annotation.StringRes
import com.pandulapeter.beagle.appDemo.R

enum class Dependency(
    val type: Type,
    val title: String,
    val copyright: String? = "Unknown",
    val url: String? = null
) {
    KOTLIN(
        type = Type.APACHE_V2,
        title = "Kotlin",
        copyright = "Copyright 2010-2020, JetBrains s.r.o.",
        url = "https://github.com/JetBrains/kotlin/tree/master/license"
    ),
    KOTLIN_COROUTINES(
        type = Type.APACHE_V2,
        title = "Kotlin Coroutines",
        copyright = "Copyright 2010-2020, JetBrains s.r.o.",
        url = "https://github.com/JetBrains/kotlin/tree/master/license"
    ),
    ANDROID_X_MULTIDEX(
        type = Type.APACHE_V2,
        title = "AndroidX MultiDex",
        copyright = "Copyright 2020, The Android Open Source Project",
        url = "https://developer.android.com/jetpack/androidx/releases/multidex"
    ),
    ANDROID_X_APP_COMPAT(
        type = Type.APACHE_V2,
        title = "AndroidX AppCompat",
        copyright = "Copyright 2020, The Android Open Source Project",
        url = "https://developer.android.com/jetpack/androidx/releases/appcompat"
    ),
    ANDROID_X_LIFECYCLE(
        type = Type.APACHE_V2,
        title = "AndroidX Lifecycle",
        copyright = "Copyright 2020, The Android Open Source Project",
        url = "https://developer.android.com/jetpack/androidx/releases/lifecycle"
    ),
    ANDROID_X_BILLING(
        type = Type.APACHE_V2,
        title = "AndroidX Billing",
        copyright = "Copyright 2020, The Android Open Source Project",
        url = "https://developer.android.com/google/play/billing"
    ),
    MATERIAL_COMPONENTS(
        type = Type.APACHE_V2,
        title = "Android Material Components",
        copyright = "Copyright 2020, The Android Open Source Project",
        url = "https://github.com/material-components/material-components-android/blob/master/LICENSE"
    ),
    CONSTRAINT_LAYOUT(
        type = Type.APACHE_V2,
        title = "Constraint Layout",
        copyright = "Copyright 2020, The Android Open Source Project",
        url = "https://developer.android.com/jetpack/androidx/releases/constraintlayout"
    ),
    RECYCLER_VIEW(
        type = Type.APACHE_V2,
        title = "Recycler View",
        copyright = "Copyright 2020, The Android Open Source Project",
        url = "https://developer.android.com/jetpack/androidx/releases/recyclerview"
    ),
    KOIN(
        type = Type.APACHE_V2,
        title = "Koin",
        url = "https://github.com/InsertKoinIO/koin"
    ),
    RETROFIT(
        type = Type.APACHE_V2,
        title = "Retrofit",
        copyright = "Copyright 2013, Square Inc.",
        url = "https://square.github.io/retrofit/"
    ),
    OK_HTTP(
        type = Type.APACHE_V2,
        title = "OkHttp",
        copyright = "Copyright 2019, Square Inc.",
        url = "https://square.github.io/okhttp/"
    ),
    MOSHI(
        type = Type.APACHE_V2,
        title = "Moshi",
        copyright = "Copyright 2015, Square Inc.",
        url = "https://github.com/square/moshi"
    ),
    GSON(
        type = Type.APACHE_V2,
        title = "Gson",
        copyright = "Copyright 2018, Google Inc.",
        url = "https://github.com/google/gson"
    ),
    KTOR(
        type = Type.APACHE_V2,
        title = "Ktor",
        url = "https://github.com/ktorio/ktor"
    ),
    LEAK_CANARY(
        type = Type.APACHE_V2,
        title = "LeakCanary",
        copyright = "Copyright 2015, Square Inc.",
        url = "https://github.com/square/leakcanary"
    );

    enum class Type(@StringRes val titleResourceId: Int) {
        APACHE_V2(R.string.licences_apache)
    }
}