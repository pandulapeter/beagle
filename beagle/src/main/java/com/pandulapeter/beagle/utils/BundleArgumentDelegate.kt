package com.pandulapeter.beagle.utils

import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Deprecated("https://github.com/pandulapeter/beagle/blob/master/metadata/MIGRATION_GUIDE.md")
internal sealed class BundleArgumentDelegate<T>(protected val key: kotlin.String, protected val defaultValue: T) : ReadWriteProperty<Bundle?, T> {

    class Boolean(key: kotlin.String, defaultValue: kotlin.Boolean = false) : BundleArgumentDelegate<kotlin.Boolean>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getBoolean(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.Boolean) = thisRef?.putBoolean(key, value) ?: Unit
    }

    class String(key: kotlin.String, defaultValue: kotlin.String = "") : BundleArgumentDelegate<kotlin.String>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getString(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: kotlin.String) = thisRef?.putString(key, value) ?: Unit
    }

    class Parcelable<P : android.os.Parcelable>(key: kotlin.String, defaultValue: P? = null) : BundleArgumentDelegate<P?>(key, defaultValue) {

        override fun getValue(thisRef: Bundle?, property: KProperty<*>) = thisRef?.getParcelable(key) as P?

        override fun setValue(thisRef: Bundle?, property: KProperty<*>, value: P?) = thisRef?.putParcelable(key, value) ?: Unit
    }
}